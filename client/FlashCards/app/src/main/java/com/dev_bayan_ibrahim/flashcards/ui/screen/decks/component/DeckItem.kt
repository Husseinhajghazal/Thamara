package com.dev_bayan_ibrahim.flashcards.ui.screen.decks.component


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.R
import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeader
import com.dev_bayan_ibrahim.flashcards.data.model.deck.colorAccent
import com.dev_bayan_ibrahim.flashcards.ui.constant.cardRatio
import com.dev_bayan_ibrahim.flashcards.ui.constant.smallCardWidth
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.DeckCard
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme


@Composable
fun DeckItem(
    modifier: Modifier = Modifier,
    deckHeader: DeckHeader,
    onClick: (() -> Unit)?,
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = modifier.padding(4.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            DeckCard(
                modifier = Modifier.aspectRatio(cardRatio),
                accent = deckHeader.colorAccent,
                onClick = onClick ?: {},
                enableClick = onClick != null,
            ) {
                LevelIcon(level = deckHeader.level)
            }
        }
        Text(
            modifier = Modifier.widthIn(max = smallCardWidth),
            text = deckHeader.name,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 2,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun LevelIcon(
    modifier: Modifier = Modifier,
    level: Int,
) {
    Icon(
        modifier = modifier.size(32.dp),
        painter =  painterResource(id = level.levelIconRes()),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurface
    )
}

private fun Int.levelIconRes(): Int {
    return R.drawable.unknown
}


@OptIn(ExperimentalLayoutApi::class)
@Preview(showBackground = true)
@Composable
private fun DeckItemPreviewLight() {
    FlashCardsTheme(darkTheme = false) {
        Surface(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.background,
        ) {
            val deck = DeckHeader(
                color =  Color.Red.toArgb(),
                cardsCount = 10,
                id = 0
            )
            val colors = listOf(
                Color(0xFF000000),
                Color(0xFF444444),
                Color(0xFF888888),
                Color(0xFFCCCCCC),
                Color(0xFFFFFFFF),
                Color(0xFFFF0000),
                Color(0xFF00FF00),
                Color(0xFF0000FF),
                Color(0xFFFFFF00),
                Color(0xFF00FFFF),
                Color(0xFFFF00FF),
            )
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                colors.forEach {
                    DeckItem(
                        deckHeader = DeckHeader(
                            name = "deck $it",
                            color =  it.toArgb(),
                            cardsCount = 10,
                            id = 0,
                        )
                    ) { }
                }
            }
        }
    }
}
