package com.dev_bayan_ibrahim.flashcards.ui.screen.decks.viewmodel

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.paging.PagingData
import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeader
import com.dev_bayan_ibrahim.flashcards.data.util.DecksFilter
import com.dev_bayan_ibrahim.flashcards.data.util.DecksGroup
import com.dev_bayan_ibrahim.flashcards.data.util.DecksOrder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


@Stable
interface DecksUiState {
    val query: String
   val groupBy: DecksGroup?
   val filterBy: DecksFilter?
   val orderBy: DecksOrder?

    val libraryDecks: Map<DecksGroup, List<DeckHeader>>
    val searchResults: StateFlow<PagingData<DeckHeader>>
}

class DecksMutableUiState : DecksUiState {
    override var query: String by mutableStateOf("")
    override var groupBy: DecksGroup? by mutableStateOf(null)
    override var filterBy: DecksFilter? by mutableStateOf(null)
    override var orderBy: DecksOrder? by mutableStateOf(null)
    override val libraryDecks: SnapshotStateMap<DecksGroup, List<DeckHeader>> = mutableStateMapOf()
    override val searchResults: MutableStateFlow<PagingData<DeckHeader>> = MutableStateFlow(PagingData.empty())
}
