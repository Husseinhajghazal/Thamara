package com.dev_bayan_ibrahim.flashcards.ui.screen.statistcs.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.data.model.statistics.TimeGroup
import com.dev_bayan_ibrahim.flashcards.data.model.statistics.TimeStatisticsItem
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TimeStatisticsItemsPager(
    modifier: Modifier = Modifier,
    items: Map<TimeGroup, TimeStatisticsItem>
) {
    val pagerState = rememberPagerState { TimeGroup.entries.count() }
    val scope = rememberCoroutineScope()
    Column(
        modifier = modifier,
    ) {
        PagerTab(selected = TimeGroup.entries[pagerState.currentPage]) {
            scope.launch {
                pagerState.animateScrollToPage(it.ordinal)
            }
        }
        HorizontalPager(state = pagerState) { page ->
            items[TimeGroup.entries[page]]?.let { item ->
                HomeTimeStatisticsItems(item = item)
            } ?: run {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "No Data",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun PagerTab(
    modifier: Modifier = Modifier,
    selected: TimeGroup,
    onSelectTab: (TimeGroup) -> Unit,
) {
    TabRow(
        modifier = modifier.fillMaxWidth(),
        selectedTabIndex = selected.ordinal,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
    ) {
        TimeGroup.entries.forEach { tab ->
            Tab(
                selected = tab == selected,
                onClick = { onSelectTab(tab) },
                selectedContentColor = MaterialTheme.colorScheme.onBackground,
                unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                text = {
                    Text(
                        text = stringResource(id = tab.nameRes),
                    )
                }
            )
        }
    }
}

@Composable
private fun HomeTimeStatisticsItems(
    modifier: Modifier = Modifier,
    item: TimeStatisticsItem,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatisticsItem(label = "plays times", value = item.plays.toString())
        StatisticsItem(label = "decks count change", value = item.decksCount.toString())
        StatisticsItem(label = "cards count change", value = item.cardsCount.toString())
        StatisticsItem(label = "correct answers", value = item.correctAnswers.toString())
        StatisticsItem(label = "incorrect answers", value = item.incorrectAnswers.toString())
        StatisticsItem(
            label = "accuracy average",
            value = "${item.answerAccuracyAverage.roundToInt()}"
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun HomeStatisticsItemPreviewLight() {
    FlashCardsTheme(darkTheme = false) {
        Surface(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.background,
        ) {
            HomeTimeStatisticsItems(
                item = TimeStatisticsItem(
                    plays = 10,
                    decksCount = 10,
                    cardsCount = 10,
                    correctAnswers = 10,
                    incorrectAnswers = 10,
                )
            )
        }
    }
}
