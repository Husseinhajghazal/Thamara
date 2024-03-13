package com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play.component


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.R
import com.dev_bayan_ibrahim.flashcards.ui.constant.smallCardHeight
import com.dev_bayan_ibrahim.flashcards.ui.constant.smallCardWidth
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.DeckCard
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme

private val cardsPadding = 8.dp

@Composable
fun CardsQueue(
    modifier: Modifier = Modifier,
    remaining: Int,
    colorAccent: Color
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth(),
    ) {
        // padding by 8
        val visibleCards by remember(maxWidth, remaining) {
            derivedStateOf {
                // #-#-#
                (maxWidth.plus(cardsPadding) / smallCardWidth.plus(cardsPadding)).toInt()
                    .coerceAtMost(remaining)
            }
        }
        val needAllWidth by remember(maxWidth) {
            derivedStateOf {
                val neededWidth = smallCardWidth.times(remaining) + cardsPadding.times(
                    remaining.dec().coerceAtLeast(0)
                )
                maxWidth < neededWidth
            }
        }
        val remainingCards by remember(visibleCards) {
            derivedStateOf { remaining - visibleCards + 1 }
        }
        Row(
            modifier = Modifier
                .height(smallCardHeight)
                .fillMaxWidth(),
            horizontalArrangement = if (needAllWidth) {
                Arrangement.SpaceBetween
            } else {
                Arrangement.spacedBy(cardsPadding, Alignment.CenterHorizontally)
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (remaining > 0) {
                repeat(visibleCards) {
                    DeckCard(accent = colorAccent) {
                        if (it == visibleCards.dec() && needAllWidth) { // last card and there are hidden cards
                            Text(
                                text = "+$remainingCards",
                                style = MaterialTheme.typography.headlineMedium
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.unknown),
                                contentDescription = null
                            )
                        }
                    }
                }
            } else {
                Text(
                    text = "No more cards",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CardsQueuePreviewLight() {
    FlashCardsTheme(darkTheme = false) {
        Surface(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.background,
        ) {
            CardsQueue(
                remaining = 10,
                colorAccent = Color.Red
            )
        }
    }
}
