package com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play.viewmodel

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.dev_bayan_ibrahim.flashcards.data.model.deck.Deck


@Stable
interface PlayUiState {
    val deck: Deck
    val currentCard: Int
}

class PlayMutableUiState : PlayUiState {
    override var deck: Deck by mutableStateOf(Deck())
    override var currentCard: Int by mutableIntStateOf(0)
}
