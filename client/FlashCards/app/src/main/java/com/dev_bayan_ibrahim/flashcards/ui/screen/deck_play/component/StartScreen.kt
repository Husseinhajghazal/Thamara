package com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeader

@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    onStart: () -> Unit,
    deckHeader: DeckHeader,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "${deckHeader.name.ifBlank { "\"No name\"" }} (${deckHeader.cardsCount} cards)",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
            )
            Text(
                text = "cards will be visible to you in random order, answer each card question according to card type",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )
            Text(
                text = buildString {
                    appendLine("1. true false cards: choose true of false according to question")
                    appendLine("2. multi-choice cards: choose the correct answer between several answers (only one is correct)")
                    appendLine("3. sentence cards: write the correct answer")
                },
                style = MaterialTheme.typography.bodyMedium,
            )
            OutlinedButton(onClick = onStart) {
                Text("Play")
            }
        }
    }
}
