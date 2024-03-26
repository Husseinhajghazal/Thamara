package com.dev_bayan_ibrahim.flashcards.ui.screen.statistcs.component


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.R
import com.dev_bayan_ibrahim.flashcards.data.model.user.UserRank
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme
import com.dev_bayan_ibrahim.flashcards.ui.util.asSimpleDate
import com.dev_bayan_ibrahim.flashcards.ui.util.daysCount
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
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

@Composable
fun RankStatisticsChart(
    modifier: Modifier = Modifier,
    ranks: List<UserRank>,
    primary: Color = MaterialTheme.colorScheme.primary,
    onBackground: Color = MaterialTheme.colorScheme.onBackground,
    labelMediumSize: TextUnit = MaterialTheme.typography.labelMedium.fontSize,
) {
    if (ranks.isNotEmpty()) {
        val xAxisData by remember(ranks) {
            derivedStateOf {
                AxisData.Builder().apply {
                    val count = ranks.getDaysDiff()
                    steps(count)
                    val first = ranks.first().datetime
                    val labels = List(count.inc()) { i ->
                        val datetime = first + i.days
                        datetime.asSimpleDate()
                    }
                    labelData { pointIndex ->
                        labels[pointIndex.coerceIn(0, count)]
                    }
                    axisStepSize(50.dp)
                    startDrawPadding(25.dp)
                    axisLineColor(onBackground)
                    axisLabelColor(onBackground)
                    backgroundColor(Color.Transparent)
                }.build()
            }
        }
        val yAxisData by remember(ranks) {
            derivedStateOf {
                AxisData.Builder().apply {
                    val maxRank = ranks.maxOf { it.asFloat() }
                    val minRank = ranks.minOf { it.asFloat() }
                    val range = (maxRank - minRank + 0.5f).roundToInt()
                    steps(minOf(range, 5))
                    labelData {
                        val percent = it.times(0.25f)
                        (range * percent + minRank).roundToInt().toString()
                    }
                    axisLineColor(onBackground)
                    axisLabelColor(onBackground)
                    startDrawPadding(35.dp)
                    backgroundColor(Color.Transparent)
                }.build()
            }
        }
        val pointsData by remember(ranks) {
            derivedStateOf {
                getLinePlotData(
                    ranks = ranks,
                    primary = primary,
                    onBackground = onBackground,
                    labelMediumSize = labelMediumSize
                )
            }
        }
        LineChart(
            modifier = modifier
                .height(200.dp)
                .fillMaxWidth(),
            lineChartData = LineChartData(
                linePlotData = pointsData,
                xAxisData = xAxisData,
                yAxisData = yAxisData,
                isZoomAllowed = false,
                backgroundColor = Color.Transparent,
            )
        )
    } else {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.no_data),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
    }
}

private fun getLinePlotData(
    ranks: List<UserRank>,
    primary: Color,
    onBackground: Color,
    labelMediumSize: TextUnit,
): LinePlotData {
    return LinePlotData(
        plotType = PlotType.Line,
        lines = listOf(
            Line(
                dataPoints = ranks.map { rank ->
                    Point(x = rank.datetime.daysCount().toFloat(), y = rank.asFloat())
                }.distinctBy { it.x },
                lineStyle = LineStyle(
                    color = primary,
                    width = 2.dp.value,
                ),
                intersectionPoint = IntersectionPoint(
                    color = primary,
                    radius = 6.dp

                ),
                selectionHighlightPoint = SelectionHighlightPoint(
                    color = primary,
                    radius = 9.dp,
                    isHighlightLineRequired = false,
                ),
                shadowUnderLine = ShadowUnderLine(
                    brush = Brush.verticalGradient(
                        listOf(
                            primary,
                            Color.Transparent,
                        )
                    )
                ),
                selectionHighlightPopUp = SelectionHighlightPopUp(
                    backgroundAlpha = 0f,
                    paddingBetweenPopUpAndPoint = 10.dp,
                    labelSize = labelMediumSize,
                    labelColor = onBackground,
                    popUpLabel = { _, y ->
                        UserRank(y).toString()
                    }
                )
            )
        )
    )
}

private fun List<UserRank>.getStep(minStep: Duration = 1.days): Duration {
    var step = minStep
    var prev: Instant? = null
    forEach { rank ->
        prev?.let {
            val diff = rank.datetime - it
            if (diff > step) step = diff
        }
        prev = rank.datetime
    }
    return step
}

private fun List<UserRank>.getDaysDiff(): Int = last().datetime.daysCount() - first().datetime.daysCount()

@Preview(showBackground = true)
@Composable
private fun RankStatisticsChartPreview() {
    FlashCardsTheme(darkTheme = false) {
        Surface(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.background,
        ) {
            val now = Clock.System.now()
            RankStatisticsChart(
                ranks = listOf(
                    UserRank(1, 1, datetime = now - 365.days),
                    UserRank(9, 2, datetime = now - 300.days),
                    UserRank(7, 1, datetime = now - 250.days),
                    UserRank(2, 10, datetime = now - 100.days),
                    UserRank(3, 50, datetime = now - 50.days),
                    UserRank(5, 32, datetime = now - 10.days),
                )
            )
        }
    }
}
