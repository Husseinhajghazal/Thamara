package com.dev_bayan_ibrahim.flashcards.ui.screen.app_design


import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme

@Composable
fun FlashDialog(
    modifier: Modifier = Modifier,
    scale: Float? = null,
    show: Boolean,
    onDismiss: () -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    if (show) {
        Dialog(
            onDismissRequest = onDismiss
        ) {
            DeckCard(
                modifier = modifier,
                scale = scale,
                accent = MaterialTheme.colorScheme.primary,
                enableClick = false,
                content = content
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FlashDialogPreviewLight() {
    FlashCardsTheme(darkTheme = false) {
        Surface(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.background,
        ) {
            FlashDialog(show = true, onDismiss = {}) {}
        }
    }
}
