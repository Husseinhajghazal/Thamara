package com.dev_bayan_ibrahim.flashcards.ui.screen.statistcs.component.line_chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.Dp


fun DrawScope.drawXAxis(
    data: XAxisLineData,
    textMeasurer: TextMeasurer,
) {
    val width = size.width
    val stepWidth = width / data.points.count()
    val lineHeight = size.height - data.xAxisVerticalPadding.toPx()
    val horizontalPadding = data.yAxisHorizontalPadding.toPx()
    drawLine(
        color = data.color,
        start = Offset(horizontalPadding, lineHeight),
        end = Offset(width, lineHeight)
    )
    repeat(data.points.count()) { step ->
        val x = horizontalPadding + stepWidth * step.inc()
        drawCircle(
            color = data.color,
            radius = data.stepIndicatorRadius.toPx(),
            center = Offset(x, lineHeight),
        )
        val text = data.getLabel(step, data.points[step].first)
        drawText(
            textLayoutResult = textMeasurer.measure(text = text, style = data.textStyle),
            topLeft = Offset(x, lineHeight)
        )
    }
}

data class XAxisLineData(
    val color: Color,
    val points: List<Pair<Float, Float>>,
    val getLabel: (Int, Float) -> String, // index + value
    val xAxisVerticalPadding: Dp,
    val yAxisHorizontalPadding: Dp,
    val stepIndicatorRadius: Dp,
    val textStyle: TextStyle
)