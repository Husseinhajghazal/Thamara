package com.dev_bayan_ibrahim.flashcards.ui.screen.statistcs

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.dev_bayan_ibrahim.flashcards.data.model.user.UserRank
import com.dev_bayan_ibrahim.flashcards.ui.app.util.FlashSnackbarVisuals
import com.dev_bayan_ibrahim.flashcards.ui.screen.statistcs.component.RankStatisticsStep
import com.dev_bayan_ibrahim.flashcards.ui.screen.statistcs.viewmodel.StatisticsViewModel
import java.util.SortedMap

@Composable
fun StatisticsRoute(
    modifier: Modifier = Modifier,
    statisticsViewModel: StatisticsViewModel = hiltViewModel(),
    onShowSnackbarMessage: (FlashSnackbarVisuals) -> Unit
) {
    val timedStatistics by statisticsViewModel.timedStatistics.collectAsState()

    val ranks : SortedMap<RankStatisticsStep, UserRank> = RankStatisticsStep.entries.associateWith {
        UserRank(1f)
    }.toSortedMap()

    StatisticsScreen(
        modifier = modifier.fillMaxSize(),
        ranks = ranks,
        timedStatistics = timedStatistics,
    )
}