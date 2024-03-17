package com.dev_bayan_ibrahim.flashcards.ui.screen.decks.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeader
import com.dev_bayan_ibrahim.flashcards.data.repo.FlashRepo
import com.dev_bayan_ibrahim.flashcards.data.util.DecksFilter
import com.dev_bayan_ibrahim.flashcards.data.util.DecksGroup
import com.dev_bayan_ibrahim.flashcards.data.util.DecksOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class DecksViewModel @Inject constructor(
    private val repo: FlashRepo,
) : ViewModel() {
    val state = DecksMutableUiState()

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
            getLibraryDecks()
        }
    }

    private var initialized = false
    fun initScreen() {
        if (!initialized) {
            getLibraryDecks()
            initialized = true
        }
    }
    private fun getLibraryDecks(
        query: String = state.query,
        groupBy: DecksGroup? = state.groupBy,
        filterBy: DecksFilter? = state.filterBy,
        orderBy: DecksOrder? = state.orderBy,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getLibraryDecks(
                query = query,
                groupBy = groupBy,
                filterBy = filterBy,
                orderBy = orderBy
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