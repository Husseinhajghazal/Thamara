package com.dev_bayan_ibrahim.flashcards.ui.screen.app_design

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashSlider(
    modifier: Modifier = Modifier,
    value: Float,
    steps: Int,
    readonly: Boolean = true,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: () -> Unit,
    valueRange: ClosedFloatingPointRange<Float>
) {
    // TODO improve the visual layout of the indicator
    val indicatorColor = MaterialTheme.colorScheme.primary
    val indicatorStyle = MaterialTheme.typography.labelLarge.copy(
        color = MaterialTheme.colorScheme.onPrimary
    )
    val valueTextMeasurer = rememberTextMeasurer()
    var showIndicator by rememberSaveable {
        mutableStateOf(false)
    }
    val onChange: (Float) -> Unit = remember {
        {
            showIndicator = true
            onValueChange(it)
        }
    }
    val onChangeFinished: () -> Unit = remember {
        {
            showIndicator = false
            onValueChangeFinished()
        }
    }
    Slider(
        modifier = modifier,
        thumb = {
            if (!readonly) {
                SliderDefaults.Thumb(
                    modifier = Modifier.drawWithCache {
                        val measuredText = valueTextMeasurer.measure(
                            text = "${value.toInt()}",
                            style = indicatorStyle
                        )
                        onDrawBehind {
                            if (showIndicator) {
                                val indicatorCenterY = -size.height - 16.dp.toPx()
                                val textY = indicatorCenterY - (measuredText.size.height / 2)
                                val textX = center.x - (measuredText.size.width / 2)
                                drawCircle(
                                    color = indicatorColor.copy(alpha = 0.16f),
                                    center = center,
                                    radius = 24.dp.toPx()
                                )
                                drawCircle(
                                    color = indicatorColor,
                                    center = center.copy(y = indicatorCenterY),
                                    radius = 16.dp.toPx()
                                )
                                drawText(
                                    textLayoutResult = measuredText,
                                    topLeft = center.copy(x = textX, y = textY)
                                )
                            }
                        }
                    },
                    interactionSource = remember { MutableInteractionSource() },
                )
            }
        },
        value = value,
        onValueChange = onChange,
        onValueChangeFinished = onChangeFinished,
        valueRange = valueRange,
        steps = steps,
        colors = if (readonly) {
            SliderDefaults.colors(
                inactiveTrackColor = Color.Transparent,
                activeTickColor = Color.Transparent,
                inactiveTickColor = Color.Transparent,
            )
        } else SliderDefaults.colors()
    )
}