package com.dev_bayan_ibrahim.flashcards.ui.screen.statistcs.component


import androidx.annotation.StringRes
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.R
import com.dev_bayan_ibrahim.flashcards.data.model.user.UserRank
import com.dev_bayan_ibrahim.flashcards.data.rank_manager.FlashRankManager
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme
import com.external.charts.axis.AxisData
import com.external.charts.common.model.PlotType
import com.external.charts.common.model.Point
import com.external.charts.ui.linechart.LineChart
import com.external.charts.ui.linechart.model.IntersectionPoint
import com.external.charts.ui.linechart.model.Line
import com.external.charts.ui.linechart.model.LineChartData
import com.external.charts.ui.linechart.model.LinePlotData
import com.external.charts.ui.linechart.model.LineStyle
import com.external.charts.ui.linechart.model.SelectionHighlightPoint
import com.external.charts.ui.linechart.model.SelectionHighlightPopUp
import com.external.charts.ui.linechart.model.ShadowUnderLine
import java.util.SortedMap
import kotlin.math.roundToInt
import kotlin.random.Random

enum class RankStatisticsStep(@StringRes val nameRes: Int) {
    YEAR(R.string.year),
    NINE_MONTHS(R.string.nine_months),
    SIX_MONTHS(R.string.six_months),
    THREE_MONTHS(R.string.three_months),
    MONTH(R.string.month),
}

@Composable
fun RankStatisticsChart(
    modifier: Modifier = Modifier,
    ranks: SortedMap<RankStatisticsStep, UserRank>
) {
    LineChart(
        modifier = modifier.height(200.dp).widthIn(max = 285.dp),
        lineChartData = LineChartData(
            linePlotData = getLinePlotData(ranks = ranks),
            xAxisData = AxisData.Builder().apply {
                steps(ranks.count())
                val labels = ranks.map { stringResource(id = it.key.nameRes) }
                labelData { labels[it.coerceIn(0, ranks.count().dec())] }
                axisStepSize(50.dp)
                startDrawPadding(25.dp)
                axisLineColor(MaterialTheme.colorScheme.onBackground)
                axisLabelColor(MaterialTheme.colorScheme.onBackground)
                backgroundColor(Color.Transparent)
            }.build(),
            yAxisData = AxisData.Builder().apply {
                val maxRank = ranks.maxOf { it.value }.rank
                val minRank = ranks.minOf { it.value }.rank
                val range = (maxRank - minRank)
                steps(minOf(range, 5))
                labelData {
                    val percent = it.times(0.25f)
                    (range * percent + minRank).roundToInt().toString()
                }
                axisLineColor(MaterialTheme.colorScheme.onBackground)
                axisLabelColor(MaterialTheme.colorScheme.onBackground)
                startDrawPadding(35.dp)
                backgroundColor(Color.Transparent)
            }.build(),
            isZoomAllowed = false,
            backgroundColor = Color.Transparent,
        )
    )
}

@Composable
private fun getLinePlotData(ranks: SortedMap<RankStatisticsStep, UserRank>): LinePlotData {
    return LinePlotData(
        plotType = PlotType.Line,
        lines = listOf(
            Line(
                dataPoints = ranks.toSortedMap(
                    compareBy { it.ordinal }
                ).map { (key, value) ->
                    Point(x = key.ordinal.toFloat(), y = value.asFloat())
                },
                lineStyle = LineStyle(
                    color = MaterialTheme.colorScheme.primary,
                    width = 2.dp.value,
                ),
                intersectionPoint = IntersectionPoint(
                    color = MaterialTheme.colorScheme.primary,
                    radius = 6.dp

                ),
                selectionHighlightPoint = SelectionHighlightPoint(
                    color = MaterialTheme.colorScheme.primary,
                    radius = 9.dp,
                    isHighlightLineRequired = false,
                ),
                shadowUnderLine = ShadowUnderLine(
                    brush = Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary,
                            Color.Transparent,
                        )
                    )
                ),
                selectionHighlightPopUp = SelectionHighlightPopUp(
                    backgroundAlpha = 0f,
                    paddingBetweenPopUpAndPoint = 10.dp,
                    labelSize = MaterialTheme.typography.labelMedium.fontSize,
                    labelColor = MaterialTheme.colorScheme.onSurface,
                    popUpLabel = { _, y ->
                        UserRank(y).toString()
                    }
                )
            )
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun RankStatisticsChartPreview() {
    FlashCardsTheme(darkTheme = false) {
        Surface(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.background,
        ) {
            RankStatisticsChart(
                ranks = RankStatisticsStep.entries.associateWith {
                    val rank = Random.nextInt(UserRank.min_rank, UserRank.top_rank)
                    val exp = Random.nextInt(0, FlashRankManager.requiredExpOfRank(rank))
                    UserRank(rank, exp)
                }.toSortedMap(compareBy { it.ordinal })
            )
        }
    }
}
