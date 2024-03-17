package com.dev_bayan_ibrahim.flashcards.ui.screen.decks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.viewmodel.DecksViewModel


@Composable
fun DecksRoute(
    modifier: Modifier = Modifier,
    decksViewModel: DecksViewModel = hiltViewModel(),
    navigateToDeckPlay: (Long) -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        decksViewModel.initScreen()
    }
    DecksScreen(
        modifier = modifier,
        state = decksViewModel.state,
        actions = decksViewModel.getDecksActions(navigateToDeckPlay = navigateToDeckPlay)
    )
}