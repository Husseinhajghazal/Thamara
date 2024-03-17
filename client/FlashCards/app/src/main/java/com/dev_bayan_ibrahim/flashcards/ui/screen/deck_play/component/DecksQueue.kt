package com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.dev_bayan_ibrahim.flashcards.data.model.deck.Deck
import com.dev_bayan_ibrahim.flashcards.data.model.deck.colorAccent
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.ExpandedCard

private const val fadeAnim = 1_000

@Composable
fun DecksQueue(
    modifier: Modifier = Modifier,
    deck: Deck,
    currentIndex: Int,
    onSelectAnswer: (String) -> Unit,
) {
    val cards by remember(deck.cards) {
        derivedStateOf { deck.cards }
    }
    val count by remember(deck.header.cardsCount) {
        derivedStateOf { deck.header.cardsCount }
    }
    val accent by remember(deck.header.colorAccent) {
        derivedStateOf {
            deck.header.colorAccent
        }
    }
    val pattern by remember(deck.header.pattern) {
        derivedStateOf {
            deck.header.pattern
        }
    }
    val reversedIndex by remember(currentIndex) {
        derivedStateOf { cards.count().dec() - currentIndex }
    }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        cards.reversed().forEachIndexed { i, card ->
            AnimatedVisibility(
                visible = i <= reversedIndex,
                exit = fadeOut(
                    animationSpec = tween(fadeAnim)
                ) + scaleOut(
                    animationSpec = tween(fadeAnim),
                    targetScale = 1.1f
                )
            ) {
                val rotationAnimatable by animateIntAsState(
                    targetValue = if (reversedIndex == i) {
                        0
                    } else {
                        count - i
//                        Random.nextInt(-10, 10)
                    },
                    animationSpec = tween(100, fadeAnim / 2),
                    label = "",
                )
                ExpandedCard(
                    modifier = Modifier
                        .graphicsLayer {
                            rotationZ = rotationAnimatable.toFloat()
                        },
                    card = card,
                    accent = accent,
                    bgPattern = pattern,
                    onSelectAnswer = onSelectAnswer
                )
            }
        }
    }
}