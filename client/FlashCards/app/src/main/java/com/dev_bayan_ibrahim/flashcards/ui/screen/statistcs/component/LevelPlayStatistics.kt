package com.dev_bayan_ibrahim.flashcards.ui.screen.statistcs.component


import android.content.Context
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.R
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.chart.getChartsColors
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.max_level
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.min_level
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme
import com.external.charts.ui.barchart.BarChart
import com.external.charts.ui.barchart.models.BarChartData
import com.external.charts.ui.barchart.models.BarStyle
import com.external.charts.ui.barchart.models.SelectionHighlightData
import kotlin.math.roundToInt
import kotlin.random.Random


@Composable
fun LevelPlayStatistics(
    modifier: Modifier = Modifier,
    levels: List<Pair<Int, Int>>,
    neutral: Color = MaterialTheme.colorScheme.onBackground,
    context: Context = LocalContext.current,
    labelMedium: TextUnit = MaterialTheme.typography.labelMedium.fontSize,
    chartColors: List<Color> = getChartsColors()
) {
    if (levels.isNotEmpty()) {
        val xAxisData by remember(levels) {
            derivedStateOf {
                barXAxis(
                    valuesCount = levels.count().inc(),
                    valueLabel = { if (this < levels.count()) toString() else "" },
                    neutral = neutral,
                )
            }
        }
        val yAxisData by remember(levels) {
            derivedStateOf {
                barYAxis(
                    maxValue = levels.maxOf { it.second }.toFloat(),
                    minValue = levels.minOf { it.second }.toFloat(),
                    neutral = neutral,
                )
            }
        }
        BarChart(
            modifier = modifier.height(200.dp),
            barChartData = BarChartData(
                chartData = barChartData(
                    values = levels,
                    colors = chartColors
                ),
                xAxisData = xAxisData,
                horizontalExtraSpace = 20.dp,
                paddingTop = 10.dp,
                yAxisData = yAxisData,
                backgroundColor = Color.Transparent,
                barStyle = BarStyle(
                    isGradientEnabled = true,
                    selectionHighlightData = SelectionHighlightData(
                        highlightTextOffset = 4.dp,
                        highlightTextSize = labelMedium,
                        highlightTextBackgroundAlpha = 0f,
                        highlightTextColor = neutral,
                        popUpLabel = { x, y ->
                            y.roundToInt().toString()
                        }
                    )
                ),
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LevelPlayStatisticsPreviewLight() {
    FlashCardsTheme(darkTheme = false) {
        Surface(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.background,
        ) {
            LevelPlayStatistics(
                levels = (min_level..max_level).map { it to Random.nextInt(1, 1000) },
            )
        }
    }
}
