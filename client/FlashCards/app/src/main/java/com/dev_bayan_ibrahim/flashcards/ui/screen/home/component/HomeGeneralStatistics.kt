@file:OptIn(ExperimentalLayoutApi::class)

package com.dev_bayan_ibrahim.flashcards.ui.screen.home.component


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.R
import com.dev_bayan_ibrahim.flashcards.data.model.statistics.GeneralStatistics
import com.dev_bayan_ibrahim.flashcards.ui.screen.statistcs.component.StatisticsItem
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme
import com.dev_bayan_ibrahim.flashcards.ui.util.asFlashPlural
import kotlin.math.roundToInt

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun HomeGeneralStatistics(
    modifier: Modifier = Modifier,
    statistics: GeneralStatistics

) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            StatisticsItem(
                label = stringResource(R.string.accuracy_average),
                value = "${statistics.accuracyAverage.roundToInt()}%"
            )
        }
        item {
            StatisticsItem(
                label = stringResource(R.string.total_decks),
                value = statistics.totalDecksCount.toString(),
            )
        }
        item {
            StatisticsItem(
                label = stringResource(R.string.total_cards),
                value = statistics.totalCardsCount.toString(),
            )
        }
        stickyHeader {
            Text(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background),
                text = stringResource(id = R.string.tags),
                style = MaterialTheme.typography.titleSmall,

            )
        }
        items(statistics.tags.toList()) {(tag, decks) ->
            Text(
                text = "$tag (${decks.asFlashPlural(id = R.plurals.deck)})"
            )
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
