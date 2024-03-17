package com.dev_bayan_ibrahim.flashcards.ui.screen.statistcs.component.line_chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.Dp
import kotlinx.collections.immutable.toPersistentList


fun DrawScope.drawChartLine(
    data: ChartLineData
) {
    val (width, height) = size.run { width to height - data.xAxisVerticalPadding.toPx() }
    val xmin = data.points.minOf { it.first }
    val xmax = data.points.maxOf { it.first }

    val ymin = data.points.minOf { it.second }
    val ymax = data.points.maxOf { it.second }

    val path = Path()
    path.moveTo(data.xAxisVerticalPadding.toPx(), size.height - data.yAxisHorizontalPadding.toPx())
    data.points.toPersistentList().add(0, 0f to 10f).forEach { (x, y) ->
        val offset = Offset(
            x = x.mapRange(xmin..xmax, data.yAxisHorizontalPadding.toPx()..width),
            y = height - y.mapRange(ymin..ymax, 0f..height)
        )
        path.lineTo(
            x = offset.x,
            y = offset.y
        )
        drawCircle(
            color = data.color,
            radius = data.pointRadius.toPx(),
            center = offset
        )
    }
    path.lineTo(width, height)
    path.lineTo(data.yAxisHorizontalPadding.toPx(), height)
    path.close()
    drawPath(
        path = path,
        brush = Brush.verticalGradient(
            listOf(
                data.color,
                Color.Transparent,
            )
        ),
        style = Fill

    )
}

data class ChartLineData(
    val points: List<Pair<Float, Float>>,
    val color: Color,
    val xAxisVerticalPadding: Dp,
    val yAxisHorizontalPadding: Dp,
    val pointRadius: Dp,
)

fun Float.mapRange(
    originRange: ClosedFloatingPointRange<Float>,
    newRange: ClosedFloatingPointRange<Float>,
): Float {
    val percent = (this - originRange.start) / originRange.len()
    return newRange.len() * percent + newRange.start
}

fun ClosedFloatingPointRange<Float>.len(): Float = endInclusive - start
