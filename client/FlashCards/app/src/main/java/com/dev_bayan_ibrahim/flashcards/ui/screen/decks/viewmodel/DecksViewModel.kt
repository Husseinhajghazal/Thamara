package com.dev_bayan_ibrahim.flashcards.ui.screen.decks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev_bayan_ibrahim.flashcards.data.model.card.Card
import com.dev_bayan_ibrahim.flashcards.data.model.deck.Deck
import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeader
import com.dev_bayan_ibrahim.flashcards.data.repo.FlashRepo
import com.dev_bayan_ibrahim.flashcards.data.util.DecksFilter
import com.dev_bayan_ibrahim.flashcards.data.util.DecksGroupType
import com.dev_bayan_ibrahim.flashcards.data.util.DecksOrderType
import com.dev_bayan_ibrahim.flashcards.data.util.DownloadStatus
import com.dev_bayan_ibrahim.flashcards.data.util.MutableDownloadStatus
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.toIntRange
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.component.DecksFilterMutableUiState
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.util.DecksDatabaseInfo
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.util.DecksTab
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.util.DecksTab.BROWSE
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.util.DecksTab.LIBRARY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _downloadStatus = MutableStateFlow<DownloadStatus?>(null)
    val downloadStatus: StateFlow<DownloadStatus?> = _downloadStatus.asStateFlow()

    fun getDecksActions(
        navigateToDeckPlay: (Long) -> Unit
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

        }

        override fun onDownloadDeck() {
            if (true/*selectedTab == BROWSE*/) {
                state.selectedDeck?.let {
                    downloadDeck(it)
                }
            }
        }

        override fun onCancelDownloadDeck() {
            if (true/*selectedTab == BROWSE*/) {
                state.selectedDeck?.let {
                    cancelDownloadDeck(it)
                    state.selectedDeck = null
                }
            }
        }

        override fun onPlayDeck() {
            if (selectedTab == LIBRARY) {
                state.selectedDeck?.let {
                    navigateToDeckPlay(it.id)
                    state.selectedDeck = null
                }
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
            BROWSE -> {

            }
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

    private fun browseDecks(
        query: String = state.query,
        groupBy: DecksGroupType? = appliedFilters.groupType,
        filterBy: DecksFilter? = appliedFilters.filter,
        orderBy: DecksOrderType? = appliedFilters.orderType,
        ascOrder: Boolean = appliedFilters.ascOrder,
    ) {

    }

    private fun downloadDeck(deck: DeckHeader) {
        viewModelScope.launch(IO) {
            _downloadStatus.value = MutableDownloadStatus {}

            getDeckCards(deck.id)?.let { cards ->
                repo.downloadDeck(Deck(deck, cards)).collect {
                    _downloadStatus.value = it
                }
            } ?: let { _downloadStatus.value = null }
            state.selectedDeck = null
        }
    }

    private suspend fun getDeckCards(id: Long): List<Card>? {
        return repo.getDeckCards(id).cards
    }

    private fun cancelDownloadDeck(deck: DeckHeader) {
        _downloadStatus.value?.cancelDownload?.let { cancel ->
            viewModelScope.launch {
                cancel()
            }
        }
        _downloadStatus.value = null
    }
}