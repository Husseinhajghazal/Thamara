package com.dev_bayan_ibrahim.flashcards.ui.screen.statistcs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.data.model.statistics.TimeGroup
import com.dev_bayan_ibrahim.flashcards.data.model.statistics.TimeStatisticsItem
import com.dev_bayan_ibrahim.flashcards.ui.screen.statistcs.component.TimeStatisticsItemsPager


@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier,
    timedStatistics: Map<TimeGroup, TimeStatisticsItem>,
) {
    Column(
        modifier = modifier.padding(8.dp),
    ) {
        TimeStatisticsItemsPager(
            modifier = Modifier,
            items = timedStatistics
        )
    }
}