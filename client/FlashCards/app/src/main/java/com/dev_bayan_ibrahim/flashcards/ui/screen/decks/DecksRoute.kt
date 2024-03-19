package com.dev_bayan_ibrahim.flashcards.ui.screen.decks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

    val dbInfo by decksViewModel.dbInfo.collectAsState()
    val downloadStatus by decksViewModel.downloadStatus.collectAsState()

    DecksScreen(
        modifier = modifier,
        state = decksViewModel.state,
        dbInfo = dbInfo,
        downloadStatus = downloadStatus,
        actions = decksViewModel.getDecksActions(navigateToDeckPlay = navigateToDeckPlay)
    )
}