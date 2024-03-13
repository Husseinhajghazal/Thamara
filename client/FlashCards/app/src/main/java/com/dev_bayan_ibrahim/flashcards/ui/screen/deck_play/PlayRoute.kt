package com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play.viewmodel.PlayViewModel

@Composable
fun PlayRoute(
    modifier: Modifier = Modifier,
    playViewModel: PlayViewModel = hiltViewModel()
) {
    PlayScreen(
        modifier = modifier,
        state = playViewModel.state,
        actions = playViewModel
    )
}