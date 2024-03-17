package com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import com.dev_bayan_ibrahim.flashcards.data.model.card.Card
import com.dev_bayan_ibrahim.flashcards.data.model.card.CardAnswer
import com.dev_bayan_ibrahim.flashcards.data.model.deck.Deck
import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeader
import com.dev_bayan_ibrahim.flashcards.data.model.deck.colorAccent
import com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play.component.DecksQueue
import com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play.component.PlayResults
import com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play.component.StartScreen
import com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play.viewmodel.PlayMutableUiState
import com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play.viewmodel.PlayStatus
import com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play.viewmodel.PlayUiActions
import com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play.viewmodel.PlayUiState
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme
import kotlin.time.Duration.Companion.seconds

@Composable
fun PlayScreen(
    modifier: Modifier = Modifier,
    state: PlayUiState,
    actions: PlayUiActions,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedVisibility(
            visible = state.status == PlayStatus.NOT_STARTED
        ) {
            StartScreen(
                onStart = actions::onStartPlay,
                deckHeader = state.deck.header
            )
        }

        AnimatedVisibility(
            visible = state.status == PlayStatus.PLAYING
        ) {
            DecksQueue(
                deck = state.deck,
                currentIndex = state.currentCard,
                onSelectAnswer = actions::onSelectAnswer
            )
        }

        AnimatedVisibility(
            visible = state.status == PlayStatus.RESULTS
        ) {
            PlayResults(
                count = state.deck.cards.count(),
                correctAnswers = state.correctAnswers,
                bgPattern = state.deck.header.pattern,
                accent = state.deck.header.colorAccent,
                incorrectCards = state.cardsAnswers,
                onRepeat = actions::onRepeat,
                onClose = actions::onClose,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlayScreenPreviewLight() {
    FlashCardsTheme(darkTheme = false) {
        Surface(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.background,
        ) {
            val state by remember {
                mutableStateOf(PlayMutableUiState())
            }
            state.deck = Deck(
                header = DeckHeader(
                    pattern = "https://drive.google.com/uc?export=download&id=1HiW96HMq-EPMLGvfsfS4Ow2VGZNTJGXt",
                    color = Color.Magenta.toArgb(),
                    cardsCount = 4
                ),
                cards = listOf(
                    Card(
                        question = "question 1",
                        image = "https://drive.google.com/uc?export=download&id=1HiRtIjas0UjWmmAqEvAhkw5wVgMGPr2O",
                        answer = CardAnswer.Info(10.seconds),
                        deckId = 0,
                    ),
                    Card(
                        question = "question 2",
                        image = "https://drive.google.com/uc?export=download&id=1HiRtIjas0UjWmmAqEvAhkw5wVgMGPr2O",
                        answer = CardAnswer.TrueFalse(true),
                        deckId = 0,
                    ),
                    Card(
                        question = "question 3",
                        image = "https://drive.google.com/uc?export=download&id=1HiRtIjas0UjWmmAqEvAhkw5wVgMGPr2O",
                        answer = CardAnswer.MultiChoice(
                            correctChoice = 3,
                            firstChoice = "choice 1, but it is too long choice, still too long, too long",
                            secondChoice = "choice 2, but it is too long choice, still too long, too long",
                            thirdChoice = "choice 3, but it is too long choice, still too long, too long"
                        ),
                        deckId = 0,
                    ),
                    Card(
                        question = "question 4",
                        image = "https://drive.google.com/uc?export=download&id=1HiRtIjas0UjWmmAqEvAhkw5wVgMGPr2O",
                        answer = CardAnswer.Write(
                            answer = "answer",
                        ),
                        deckId = 0,
                    ),
                )
            )
            val actions = object : PlayUiActions {
                override fun onSelectAnswer(value: String) {
                    val card = state.deck.cards[state.currentCard]
                    if (card.answer.checkIfCorrect(value)) {
                        state.correctAnswers += 1
                    } else {
                        state.cardsAnswers[card] = value
                    }
                    if (state.currentCard == state.deck.cards.count().dec()) {
                        state.status = PlayStatus.RESULTS
                    } else {
                        state.currentCard += 1
                    }
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

                override fun onClose() {
                    throw Exception()
                }
            }
            PlayScreen(
                state = state,
                actions = actions,
            )
        }
    }
}
