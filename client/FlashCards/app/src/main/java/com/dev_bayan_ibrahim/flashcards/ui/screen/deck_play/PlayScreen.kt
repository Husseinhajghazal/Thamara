package com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play


import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.dev_bayan_ibrahim.flashcards.data.model.card.Card
import com.dev_bayan_ibrahim.flashcards.data.model.card.CardAnswer
import com.dev_bayan_ibrahim.flashcards.data.model.deck.Deck
import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeader
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.ExpandedCard
import com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play.component.CardsQueue
import com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play.viewmodel.PlayMutableUiState
import com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play.viewmodel.PlayUiActions
import com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play.viewmodel.PlayUiState
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme

@Composable
fun PlayScreen(
    modifier: Modifier = Modifier,
    state: PlayUiState,
    actions: PlayUiActions,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CardsQueue(
            remaining = state.deck.header.cardsCount - state.currentCard.inc(),
            colorAccent = state.deck.header.colorAccent
        )
        ExpandedCard(
            modifier = Modifier.weight(1f),
            card = state.deck.cards[state.currentCard],
            accent = state.deck.header.colorAccent,
            bgPattern = state.deck.header.pattern,
            onSelectAnswer = actions::onSelectAnswer
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreviewLight() {
    FlashCardsTheme(darkTheme = false) {
        Surface(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.background,
        ) {
            val state = PlayMutableUiState()
            state.deck = Deck(
                header = DeckHeader(
                    pattern = "https://drive.google.com/uc?export=download&id=1HiW96HMq-EPMLGvfsfS4Ow2VGZNTJGXt",
                    colorAccent = Color.Magenta,
                    cardsCount = 10
                ),
                cards = List(10){
                    Card(
                        question = "this is a cat",
                        image = "https://drive.google.com/uc?export=download&id=1HiRtIjas0UjWmmAqEvAhkw5wVgMGPr2O",
                        answer = CardAnswer.Write(
                            "correct answer"
                        )
                    )
                }
            )
            val actions = object : PlayUiActions {
                override fun onSelectAnswer(value: String) {
                }
            }
            PlayScreen(
                state = state,
                actions = actions,
            )
        }
    }
}
