package com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.data.model.card.Card
import com.dev_bayan_ibrahim.flashcards.ui.constant.cardRatio
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.FlashSlider
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.IncorrectExpandedCard
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private const val resultAnim = 500
private const val resultAnimDelay = 1000

@Composable
fun PlayResults(
    modifier: Modifier = Modifier,
    count: Int,
    correctAnswers: Int,
    incorrectCards: Map<Card, String?>, // card, answer
    accent: Color,
    bgPattern: String,
    onRepeat: () -> Unit,
    onClose: () -> Unit,
) {
    val animatable by remember {
        mutableStateOf(Animatable(0f))
    }
    LaunchedEffect(key1 = Unit) {
        animatable.animateTo(
            targetValue = correctAnswers.toFloat() / count * 100,
            animationSpec = tween(resultAnim, resultAnimDelay)
        )
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "${animatable.value.roundToInt()}%",
            style = MaterialTheme.typography.displayMedium
        )
        FlashSlider(
            value = animatable.value,
            steps = 0,
            onValueChange = {},
            onValueChangeFinished = { },
            valueRange = 0f..100f
        )
        Text(
            text = "you answered $correctAnswers correct answer from $count",
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = "incorrect card ${count - correctAnswers}",
            style = MaterialTheme.typography.bodyMedium
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(onClick = onClose) {
                Text(text = "Close")
            }
            OutlinedButton(onClick = onRepeat) {
                Text(text = "Repeat")
            }
        }
        IncorrectCardsPager(
            cards = incorrectCards.mapNotNull { (key, value) ->
                value?.let { key to value }
            },
            accent = accent,
            bgPattern = bgPattern
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun IncorrectCardsPager(
    modifier: Modifier = Modifier,
    cards: List<Pair<Card, String>>, // card, answer
    accent: Color,
    bgPattern: String,
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState { cards.count() }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
    ) {
        IconButton(
            enabled = pagerState.currentPage > 0,
            onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(
                        pagerState.currentPage.dec().coerceAtLeast(0)
                    )
                }
            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Previous"
            )
        }
        HorizontalPager(
            modifier = Modifier.aspectRatio(cardRatio),
            state = pagerState,
            userScrollEnabled = false,
        ) { i ->
            val (card, answer) = cards[i]
            Box(
                modifier = Modifier,
            ) {
                IncorrectExpandedCard(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    card = card,
                    accent = accent,
                    bgPattern = bgPattern,
                    incorrectAnswer = answer
                )
            }
        }

        IconButton(
            enabled = pagerState.currentPage < cards.count().dec(),
            onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(
                        pagerState.currentPage.inc().coerceAtMost(cards.count().dec())
                    )
                }
            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Previous"
            )
        }
    }
}
