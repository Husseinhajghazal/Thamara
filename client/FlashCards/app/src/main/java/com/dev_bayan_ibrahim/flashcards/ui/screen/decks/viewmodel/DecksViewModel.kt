package com.dev_bayan_ibrahim.flashcards.ui.screen.decks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev_bayan_ibrahim.flashcards.data.repo.FlashRepo
import com.dev_bayan_ibrahim.flashcards.data.util.DecksFilter
import com.dev_bayan_ibrahim.flashcards.data.util.DecksGroupType
import com.dev_bayan_ibrahim.flashcards.data.util.DecksOrderType
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.toIntRange
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.component.DecksFilterMutableUiState
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.util.DecksTab
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.util.DecksTab.BROWSE
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.util.DecksTab.LIBRARY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DecksViewModel @Inject constructor(
    private val repo: FlashRepo,
) : ViewModel() {
    val state = DecksMutableUiState()

    var appliedFilters = DecksFilterMutableUiState()
    var selectedTab = LIBRARY

    fun getDecksActions(
        navigateToDeckPlay: (Long) -> Unit
    ): DecksUiActions = object : DecksUiActions {
        override fun onSearchQueryChange(query: String) {
            state.query = query
            getLibraryDecks()
        }

        override fun onClickDeck(id: Long) {
            navigateToDeckPlay(id)
        }

        override fun onSearch() {
            onApplyFilters()
        }

        override fun onSelectTab(tab: DecksTab) {
            selectedTab = tab

        }

        override fun onSelectGroupType(groupType: DecksGroupType) {
            state.filterDialogState.groupType = groupType
        }

        override fun onSelectOrderType(orderType: DecksOrderType) {
            state.filterDialogState.orderType = orderType
        }

        override fun onSelectTag(tag: String) {
            state.filterDialogState.run {
                val tags = if (tag in (filterType.tags)) {
                    filterType.tags.remove(tag)
                } else {
                    filterType.tags.add(tag)
                }
                filterType = filterType.copy(tags = tags)
            }
        }

        override fun onDeselectAll() {
            state.filterDialogState.run {
                filterType = filterType.copy(tags = persistentSetOf())
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
            state.filterDialogState.selectedLevelsRange = levels.toIntRange()
        }

        override fun onRateValueChange(rates: ClosedFloatingPointRange<Float>) {
            state.filterDialogState.selectedRateRange = rates
        }

    }

    private var initialized = false
    fun initScreen() {
        viewModelScope.launch {
            if (!initialized) {
                val levels = repo.getLevelsRange() ?: 1..10
                state.filterDialogState.levelsRange = levels
                appliedFilters.levelsRange = levels
                getLibraryDecks()
            }
            initialized = true
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
        filterBy: DecksFilter? = appliedFilters.filterType,
        orderBy: DecksOrderType? = appliedFilters.orderType,
        ascOrder: Boolean = appliedFilters.ascOrder,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
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
}