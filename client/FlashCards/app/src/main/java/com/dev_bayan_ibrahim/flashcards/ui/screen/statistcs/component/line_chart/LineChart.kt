package com.dev_bayan_ibrahim.flashcards.ui.screen.statistcs.component.line_chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme

@Composable
fun LineChart(
    modifier: Modifier = Modifier,
    points: List<Pair<Float, Float>>,
    color: Color = MaterialTheme.colorScheme.primary,
    xLabel: (Int, Float) -> String,
    yLabel: (Int, Float) -> String,
) {
    val background = LineChartBackground(
        bgColor = MaterialTheme.colorScheme.background,
        linesColor = MaterialTheme.colorScheme.onSurfaceVariant,
        gridCells = 4,
    )
    val textMeasurer = rememberTextMeasurer()
    val curve = ChartLineData(
        points = points,
        color = color,
        xAxisVerticalPadding = 20.dp,
        yAxisHorizontalPadding = 20.dp,
        pointRadius = 3.dp
    )
    val xAxisData = XAxisLineData(
        color = MaterialTheme.colorScheme.onBackground,
        points = points,
        getLabel = xLabel,
        xAxisVerticalPadding = 20.dp,
        yAxisHorizontalPadding = 20.dp,
        stepIndicatorRadius = 2.dp,
        textStyle = MaterialTheme.typography.labelSmall,
    )
    val yAxisData = YAxisLineData(
        color = MaterialTheme.colorScheme.onBackground,
        min = points.minOf { it.second },
        max = points.maxOf { it.second },
        stepsCount = points.count().coerceAtMost(5),
        getLabel = yLabel,
        xAxisVerticalPadding = 20.dp,
        yAxisHorizontalPadding = 20.dp,
        stepIndicatorRadius = 2.dp,
        textStyle = MaterialTheme.typography.labelSmall,
    )
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        clipRect (
            left = 20.dp.toPx(),
            bottom = size.height - 20.dp.toPx(),

        ) {
            drawLineChartBackground(background)
        }
        drawChartLine(curve)
        drawXAxis(xAxisData, textMeasurer)
        drawYAxis(yAxisData, textMeasurer)
    }
}

@Preview(showBackground = false)
@Composable
private fun LineChartPreview() {

    FlashCardsTheme {
        Surface {
            Box(
                modifier = Modifier.size(600.dp),
                contentAlignment = Alignment.Center
            ) {
                LineChart(
                    points = listOf(
                        1f to 1f,
                        2f to 2f,
                        3f to 7f,
                        4f to 4f,
                        5f to 5f,
                        6f to 6f,
                    ),
                    xLabel = { i, f -> "label $f" },
                    yLabel = { i, f -> "label $f" }
                )
            }
        }
    }
}