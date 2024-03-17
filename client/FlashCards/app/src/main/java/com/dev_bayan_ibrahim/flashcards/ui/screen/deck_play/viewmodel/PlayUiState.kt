package com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play.viewmodel

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.dev_bayan_ibrahim.flashcards.data.model.card.Card
import com.dev_bayan_ibrahim.flashcards.data.model.deck.Deck


@Stable
interface PlayUiState {
    val deck: Deck
    val currentCard: Int
    val status: PlayStatus
    val cardsAnswers: Map<Card, String?>
    val correctAnswers: Int
}

class PlayMutableUiState : PlayUiState {
    override var deck: Deck by mutableStateOf(Deck())
    override var currentCard: Int by mutableIntStateOf(0)
    override var status: PlayStatus by mutableStateOf(PlayStatus.NOT_STARTED)
    override val cardsAnswers: SnapshotStateMap<Card, String?> = mutableStateMapOf()
    override var correctAnswers: Int by mutableIntStateOf(0)
}
enum class PlayStatus {
    NOT_STARTED,
    PLAYING,
    RESULTS
}
