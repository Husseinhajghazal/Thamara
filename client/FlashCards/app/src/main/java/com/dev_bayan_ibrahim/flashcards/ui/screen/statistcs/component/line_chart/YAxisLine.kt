package com.dev_bayan_ibrahim.flashcards.ui.screen.statistcs.component.line_chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.Dp

fun DrawScope.drawYAxis(
    data: YAxisLineData,
    textMeasurer: TextMeasurer,
) {
    val height = size.height - data.xAxisVerticalPadding.toPx()
    val stepHeight = height / data.stepsCount.inc()
    val dataRange = data.max - data.min
    val valueStep = dataRange / data.stepsCount
    drawLine(
        color = data.color,
        start = Offset(data.yAxisHorizontalPadding.toPx(), 0f),
        end = Offset(data.yAxisHorizontalPadding.toPx(), height)
    )
    repeat(data.stepsCount) { step ->
        val h = height - (step.inc() * stepHeight)
        drawCircle(
            color = data.color,
            radius = data.stepIndicatorRadius.toPx(),
            center = Offset(data.yAxisHorizontalPadding.toPx(), h),
        )
        val text = data.getLabel(step, step * valueStep + data.min)
        drawText(
            textLayoutResult = textMeasurer.measure(text = text, style = data.textStyle),
            topLeft = Offset(0f, h),
        )
    }
}

data class YAxisLineData(
    val color: Color,
    val min: Float,
    val max: Float,
    val stepsCount: Int,
    val getLabel: (Int, Float) -> String, // index + value
    val xAxisVerticalPadding: Dp,
    val yAxisHorizontalPadding: Dp,
    val stepIndicatorRadius: Dp,
    val textStyle: TextStyle
)
