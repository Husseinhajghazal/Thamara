package com.dev_bayan_ibrahim.flashcards.ui.screen.decks.viewmodel

import androidx.compose.runtime.Immutable

@Immutable
interface DecksUiActions {
    fun onSearchQueryChange (query: String)
    fun onClickDeck(id: Long)
    fun onSearch()
}