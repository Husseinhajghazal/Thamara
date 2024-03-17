package com.dev_bayan_ibrahim.flashcards.ui.screen.statistcs.component.line_chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope


fun DrawScope.drawLineChartBackground(data: LineChartBackground) {
    if (data.gridCells > 1) {
        drawRect(data.bgColor)
        drawGridCells(data.linesColor, data.gridCells)
    }
}

fun DrawScope.drawGridCells(
    color: Color,
    cells: Int,
) {
    val (width, height) = size
    val unitWidth = width / cells
    val unitHeight = height / cells
    repeat(cells.dec()) { i ->
        drawLine(
            color = color,
            start = Offset(0f, i.inc() * unitHeight),
            end = Offset(width, i.inc() * unitHeight)
        )
        drawLine(
            color = color,
            start = Offset(i.inc() * unitWidth, 0f),
            end = Offset(i.inc() * unitWidth, height)
        )
    }
}

data class LineChartBackground(
    val bgColor: Color,
    val linesColor: Color,
    val gridCells: Int
)
