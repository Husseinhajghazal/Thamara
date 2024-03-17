package com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev_bayan_ibrahim.flashcards.data.model.card.Card
import com.dev_bayan_ibrahim.flashcards.data.repo.FlashRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayViewModel @Inject constructor(
    private val repo: FlashRepo
) : ViewModel() {
    val state = PlayMutableUiState()

    fun initScreen(deckId: Long) {
        if (deckId != state.deck.header.id) {
            viewModelScope.launch {
                state.deck = repo.getDeckCards(deckId)
            }
        }
    }

    private fun handleAnswer(card: Card, answer: String) {
        if (card.answer.checkIfCorrect(answer)) {
            state.correctAnswers += 1
        } else {
            state.cardsAnswers[card] = answer
        }
    }

    private fun handleNextCard() {
        if (state.currentCard == state.deck.cards.count().dec()) {
            state.status = PlayStatus.RESULTS
            handleSavePlayRecord()
        } else {
            state.currentCard += 1
        }
    }

    private fun handleSavePlayRecord() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.saveDeckResults(
                state.deck.header.id!!,
                state.cardsAnswers.mapKeys { (card, _) ->
                    card.id!!
                }.mapValues { (_, answer) ->
                    answer == null
                }
            )
        }
    }

    fun getUiActions(
        navigateUp: () -> Unit,
    ): PlayUiActions = object : PlayUiActions {
        override fun onSelectAnswer(value: String) {
            val card = state.deck.cards[state.currentCard]
            handleAnswer(card, value)
            handleNextCard()
        }

        override fun onStartPlay() {
            state.status = PlayStatus.PLAYING
        }

        override fun onRepeat() {
            state.status = PlayStatus.NOT_STARTED
            state.currentCard = 0
            state.correctAnswers = 0
            state.cardsAnswers.clear()
        }

        override fun onClose() = navigateUp()
    }
}