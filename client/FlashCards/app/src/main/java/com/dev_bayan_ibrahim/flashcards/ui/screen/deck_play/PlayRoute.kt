package com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.dev_bayan_ibrahim.flashcards.data.util.invalid_id
import com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play.viewmodel.PlayViewModel

@Composable
fun PlayRoute(
    modifier: Modifier = Modifier,
    playViewModel: PlayViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    id: Long,
) {
    LaunchedEffect(key1 = id) {
        playViewModel.initScreen(id)

    }

    if (playViewModel.state.deck.header.let { it.id != null && it.id != invalid_id}) {
        PlayScreen(
            modifier = modifier,
            state = playViewModel.state,
            actions = playViewModel.getUiActions(navigateUp = navigateUp)
        )
    }
}