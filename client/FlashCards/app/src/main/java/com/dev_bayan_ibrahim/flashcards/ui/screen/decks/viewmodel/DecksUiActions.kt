package com.dev_bayan_ibrahim.flashcards.ui.screen.decks.viewmodel

import androidx.compose.runtime.Immutable
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.component.DecksFilterDialogUiActions
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.util.DecksTab

@Immutable
interface DecksUiActions: DecksFilterDialogUiActions {
    fun onSearchQueryChange (query: String)
    fun onClickDeck(id: Long)
    fun onSearch()
    fun onSelectTab(tab: DecksTab)
}