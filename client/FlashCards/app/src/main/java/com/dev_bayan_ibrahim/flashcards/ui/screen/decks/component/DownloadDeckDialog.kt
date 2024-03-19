package com.dev_bayan_ibrahim.flashcards.ui.screen.decks.component


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.R
import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeader
import com.dev_bayan_ibrahim.flashcards.data.util.DownloadStatus
import com.dev_bayan_ibrahim.flashcards.data.util.MutableDownloadStatus
import com.dev_bayan_ibrahim.flashcards.data.util.toFormattedSize
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme

@Composable
fun DownloadDeckDialog(
    modifier: Modifier = Modifier,
    show: Boolean,
    deck: DeckHeader,
    downloadStatus: DownloadStatus?,
    onDownload: () -> Unit,
    onCancel: () -> Unit,
) {
    DecksListDeckDialog(
        modifier = modifier,
        show = show,
        onDismiss = {},
        deck = deck
    ) {
        DownloadActions(
            downloadStatus = downloadStatus,
            onCancel = onCancel,
            onDownload = onDownload
        )
    }
}

@Composable
private fun DownloadActions(
    modifier: Modifier = Modifier,
    downloadStatus: DownloadStatus?,
    onCancel: () -> Unit,
    onDownload: () -> Unit,
) {
    if (downloadStatus != null) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(40.dp),
//                    progress = { (downloadStatus.progress.toFloat()) / downloadStatus.total.coerceAtLeast(1) }
                )
                IconButton(
                    onClick = onCancel
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close"
                    )
                }
            }
//            Text(
//                text = "${downloadStatus.progress.formatSize()}/${downloadStatus.total.formatSize()}",
//                style = MaterialTheme.typography.bodyLarge
//            )
        }
    } else {
        FilledIconButton(
            modifier = modifier,
            onClick = onDownload
        ) {
            Icon(
                painter = painterResource(id = R.drawable.download),
                contentDescription = "Download"
            )
        }
    }
}

@Composable
private fun Long?.formatSize(): String = (this ?: 0).toFormattedSize().getValue()
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
