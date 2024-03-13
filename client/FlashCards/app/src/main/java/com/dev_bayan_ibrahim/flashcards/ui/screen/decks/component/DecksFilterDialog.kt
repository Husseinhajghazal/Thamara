package com.dev_bayan_ibrahim.flashcards.ui.screen.decks.component


import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme

@Composable
fun DecksFilterDialog(
    modifier: Modifier = Modifier,
    show: Boolean
) {
    // TODO add DecksFilterDialog
}

@Preview(showBackground = true)
@Composable
private fun DecksFilterDialogPreviewLight() {
    FlashCardsTheme(darkTheme = false) {
        Surface(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.background,
        ) {
            DecksFilterDialog(show = true)
        }
    }
}
