package com.dev_bayan_ibrahim.flashcards.ui.screen.decks.component


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.R
import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeader
import com.dev_bayan_ibrahim.flashcards.data.util.MutableDownloadStatus
import com.dev_bayan_ibrahim.flashcards.ui.constant.cardRatio
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.FlashDialog
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme

@Composable
fun DecksListDeckDialog(
    modifier: Modifier = Modifier,
    show: Boolean,
    deck: DeckHeader,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    FlashDialog(
        modifier = modifier
            .height(400.dp)
            .aspectRatio(cardRatio),
        show = show,
        onDismiss = onDismiss,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DeckItem(
                modifier = Modifier
                    .height(100.dp)
                    .aspectRatio(cardRatio),
                deckHeader = deck,
                onClick = null
            )
            Text(
                text = deck.name,
                style = MaterialTheme.typography.titleMedium
            )
            InfoItems(
                rate = deck.rate,
                rates = deck.rates,
                level = deck.level,
                cardsCount = deck.cardsCount,
                tags = deck.tags
            )
            content()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun InfoItems(
    modifier: Modifier = Modifier,
    rate: Float,
    rates: Int,
    level: Int,
    cardsCount: Int,
    tags: List<String>,
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        InfoItem(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = R.string.rate),
            value = stringResource(R.string.x_rate_y_rates, rate, rates)
        )
        InfoItem(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = R.string.level),
            value = level.toString()
        )
        InfoItem(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(R.string.cards),
            value = stringResource(R.string.x_cards, cardsCount)
        )
        if (tags.isNotEmpty()) {
            InfoItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .basicMarquee(Int.MAX_VALUE),
                label = stringResource(R.string.tags),
                value = tags.joinToString(" - ")
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun InfoItem(
    modifier: Modifier = Modifier,
    label: String,
    value: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = label, style = MaterialTheme.typography.labelMedium)
//        Spacer(modifier = Modifier.weight(1f))
        Text(
            modifier = Modifier.basicMarquee(Int.MAX_VALUE),
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
@Preview(showBackground = true)
@Composable
private fun DownloadDeckDialogPreviewLight() {
    FlashCardsTheme(darkTheme = false) {
        Surface(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.background,
        ) {
            val downloadStatus = MutableDownloadStatus {}
            DownloadDeckDialog(
                deck = DeckHeader(
                    name = "name",
                    level = 5,
                    rate = 1.4f,
                    rates = 100,
                    cardsCount = 5,
                    id = 0,
                ),
                downloadStatus = downloadStatus,
                onCancel = {},
                onDownload = {},
                show = true
            )
        }
    }
}
