@file:OptIn(ExperimentalLayoutApi::class)

package com.dev_bayan_ibrahim.flashcards.ui.screen.home.component


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.data.model.statistics.GeneralStatistics
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme
import kotlin.math.roundToInt

@Composable
fun HomeGeneralStatistics(
    modifier: Modifier = Modifier,
    statistics: GeneralStatistics

) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HomeStatisticsItem(
            label = "accuracy average",
            value = "${statistics.accuracyAverage.roundToInt()}%"
        )
        HomeStatisticsItem(
            label = "total decks",
            value = statistics.totalDecksCount.toString(),
        )
        HomeStatisticsItem(
            label = "total cards",
            value = statistics.totalCardsCount.toString(),
        )
        Column {
            Text("Tags", style = MaterialTheme.typography.titleSmall)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                statistics.tags.forEach { (tag, decks) ->
                    Text(
                        text = "$tag ($decks decks)"
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeGeneralStatisticsPreviewLight() {
    FlashCardsTheme(darkTheme = false) {
        Surface(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.background,
        ) {
            HomeGeneralStatistics(
                statistics = GeneralStatistics(
                    tags = mapOf(
                        "tag 1" to 1,
                        "tag 2" to 100,
                        "tag 3" to 10000
                    ),
                    10f,
                    100_000,
                    1_000_000
                )
            )
        }
    }
}
