package com.dev_bayan_ibrahim.flashcards.ui.screen.decks.viewmodel

import androidx.compose.runtime.Immutable
import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeader
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.component.DecksFilterDialogUiActions
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.util.DecksTab

@Immutable
interface DecksUiActions: DecksFilterDialogUiActions {
    fun onSearchQueryChange (query: String)
    fun onClickDeck(deck: DeckHeader)
    fun onSearch()
    fun onSelectTab(tab: DecksTab)

    fun onDownloadDeck()
    fun onCancelDownloadDeck()
    fun onPlayDeck()
    fun onDismissSelectedDeck()
}