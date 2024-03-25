package com.dev_bayan_ibrahim.flashcards.ui.screen.decks.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.dev_bayan_ibrahim.flashcards.data.model.deck.Deck
import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeader
import com.dev_bayan_ibrahim.flashcards.data.repo.FlashRepo
import com.dev_bayan_ibrahim.flashcards.data.util.DecksFilter
import com.dev_bayan_ibrahim.flashcards.data.util.DecksGroupType
import com.dev_bayan_ibrahim.flashcards.data.util.DecksOrderType
import com.dev_bayan_ibrahim.flashcards.data.util.DownloadStatus
import com.dev_bayan_ibrahim.flashcards.data.util.MutableDownloadStatus
import com.dev_bayan_ibrahim.flashcards.ui.app.util.FlashSnackbarMessages
import com.dev_bayan_ibrahim.flashcards.ui.app.util.FlashSnackbarVisuals
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.toIntRange
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.component.DecksFilterMutableUiState
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.util.DecksDatabaseInfo
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.util.DecksTab
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.util.DecksTab.BROWSE
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.util.DecksTab.LIBRARY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class DecksViewModel @Inject constructor(
    private val repo: FlashRepo,
    private val filesDispatcher: CoroutineContext,
) : ViewModel() {
    val state = DecksMutableUiState()

    var appliedFilters = DecksFilterMutableUiState()
    var selectedTab = LIBRARY

    val dbInfo = repo.getDatabaseInfo().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        DecksDatabaseInfo(allTags = setOf())
    )
    val libraryDecksIds = repo.getLibraryDecksIds().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        mapOf()
    )

    var downloadStatus: DownloadStatus? by mutableStateOf(null)
        private set


    fun getDecksActions(
        navigateToDeckPlay: (Long) -> Unit,
        onShowSnackbarMessage: (FlashSnackbarVisuals) -> Unit,
    ): DecksUiActions = object : DecksUiActions {
        override fun onSearchQueryChange(query: String) {
            state.query = query
            getLibraryDecks()
        }

        override fun onClickDeck(deck: DeckHeader) {
            state.selectedDeck = deck
        }

        override fun onSearch() {
            onApplyFilters()
        }

        override fun onSelectTab(tab: DecksTab) {
            selectedTab = tab
            onApplyFilters()
        }

        override fun onDownloadDeck() {
            if (selectedTab == BROWSE) {
                state.selectedDeck?.let {
                    downloadDeck(deck = it, onShowSnackbarMessage) { error ->
                        if (error == null) {
                            onShowSnackbarMessage(
                                FlashSnackbarMessages.FinishDownloadDeck
                            )
                        } else {
                            onShowSnackbarMessage(FlashSnackbarMessages.Factory(error))
                        }
                    }
                }
            }
        }

        override fun onCancelDownloadDeck() {
            if (selectedTab == BROWSE) {
                state.selectedDeck?.let {
                    cancelDownloadDeck(it)
                    state.selectedDeck = null
                }
            }
        }

        override fun onPlayDeck() {
            state.selectedDeck?.let {
                navigateToDeckPlay(it.id)
                state.selectedDeck = null
            }
        }

        override fun onDismissSelectedDeck() {
            if (selectedTab == LIBRARY) {
                state.selectedDeck = null
            }
        }

        override fun onSelectGroupType(groupType: DecksGroupType) {
            state.filterDialogState.groupType = groupType
        }

        override fun onSelectOrderType(orderType: DecksOrderType) {
            state.filterDialogState.orderType = orderType
        }

        override fun onSelectTag(tag: String) {
            state.filterDialogState.run {
                val tags = if (tag in (filter.tags)) {
                    filter.tags.remove(tag)
                } else {
                    filter.tags.add(tag)
                }
                filter = filter.copy(tags = tags)
            }
        }

        override fun onDeselectAll() {
            state.filterDialogState.run {
                filter = filter.copy(tags = persistentSetOf())
            }
        }

        override fun onShowDialog() {
            state.filterDialogState.show = true
        }

        override fun changeOrderType(asc: Boolean) {
            state.filterDialogState.ascOrder = asc
        }

        override fun onApply() {
            state.filterDialogState.show = false
            appliedFilters = state.filterDialogState
            onApplyFilters()
        }

        override fun onCancel() {
            state.filterDialogState.show = false
            state.filterDialogState.onApply(appliedFilters)
        }

        override fun onReset() {
            state.filterDialogState.reset()
        }

        override fun onLevelsValueChange(levels: ClosedFloatingPointRange<Float>) {
            state.filterDialogState.run {
                filter = filter.copy(levels = levels.toIntRange())
            }
        }

        override fun onRateValueChange(rates: ClosedFloatingPointRange<Float>) {
            state.filterDialogState.run {
                filter = filter.copy(rate = rates)
            }
        }
    }

    private var initialized = false
    fun initScreen() {
        viewModelScope.launch {
            if (!initialized) {
                getLibraryDecks()
                initialized = true
            }
        }
    }

    private fun onApplyFilters() {
        when (selectedTab) {
            LIBRARY -> getLibraryDecks()
            BROWSE -> getBrowseDecks()
        }
    }

    private fun getLibraryDecks(
        query: String = state.query,
        groupBy: DecksGroupType? = appliedFilters.groupType,
        filterBy: DecksFilter? = appliedFilters.filter,
        orderBy: DecksOrderType? = appliedFilters.orderType,
        ascOrder: Boolean = appliedFilters.ascOrder,
    ) {
        viewModelScope.launch(filesDispatcher) {
            repo.getLibraryDecks(
                query = query,
                groupBy = groupBy,
                filterBy = filterBy,
                orderBy = orderBy?.toDeckOrder(ascOrder)
            ).distinctUntilChanged()
                .collect { decks ->
                    state.libraryDecks.apply {
                        clear()
                        putAll(decks)
                    }
                }
        }
    }

    private fun getBrowseDecks(
        query: String = state.query,
        groupBy: DecksGroupType? = appliedFilters.groupType,
        filterBy: DecksFilter? = appliedFilters.filter,
        orderBy: DecksOrderType? = appliedFilters.orderType,
        ascOrder: Boolean = appliedFilters.ascOrder,
    ) {
        viewModelScope.launch {
            repo.getBrowseDecks(
                query = query,
                groupBy = groupBy,
                filterBy = filterBy,
                orderBy = orderBy?.toDeckOrder(ascOrder)
            ).distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect { pagingData ->
                    state.searchResults.value = pagingData
                }
        }
    }

    private fun downloadDeck(
        deck: DeckHeader,
        onShowSnackbarMessage: (FlashSnackbarVisuals) -> Unit,
        onFinish: (Throwable?) -> Unit = {}
    ) {
        viewModelScope.launch(IO) {
            downloadStatus = MutableDownloadStatus {} // start loading indicator

            getDeckInfo(deck.id, onShowSnackbarMessage)?.let { deck ->
                repo.downloadDeck(deck).collect {
                    downloadStatus = it
                }
            } ?: let {
                downloadStatus = null
            }

            onFinish(downloadStatus?.error)
            state.selectedDeck = null
            downloadStatus = null
        }
    }

    private suspend fun getDeckInfo(
        id: Long,
        onShowSnackbarMessage: (FlashSnackbarVisuals) -> Unit
    ): Deck? {
        return repo.getDeckInfo(id).fold(
            onSuccess = {
                it
            },
            onFailure = { error ->
                onShowSnackbarMessage(FlashSnackbarMessages.Factory(error))
                null
            }
        )
    }

    private fun cancelDownloadDeck(deck: DeckHeader) {
        downloadStatus?.cancelDownload?.let { cancel ->
            viewModelScope.launch {
                cancel()
            }
        }
        downloadStatus = null
    }
}