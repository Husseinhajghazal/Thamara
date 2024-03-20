package com.dev_bayan_ibrahim.flashcards.ui.screen.statistcs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.dev_bayan_ibrahim.flashcards.ui.app.util.FlashSnackbarVisuals
import com.dev_bayan_ibrahim.flashcards.ui.screen.statistcs.viewmodel.StatisticsViewModel

@Composable
fun StatisticsRoute(
    modifier: Modifier = Modifier,
    statisticsViewModel: StatisticsViewModel = hiltViewModel(),
    onShowSnackbarMessage: (FlashSnackbarVisuals) -> Unit
) {
    val timedStatistics by statisticsViewModel.timedStatistics.collectAsState()

    StatisticsScreen(
        modifier = modifier,
        timedStatistics = timedStatistics
    )
}