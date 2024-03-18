package com.dev_bayan_ibrahim.flashcards.ui.screen.app_design

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.ui.constant.smallCardHeight
import com.dev_bayan_ibrahim.flashcards.ui.constant.smallCardWidth

@Composable
fun DeckCard(
    modifier: Modifier = Modifier,
    accent: Color,
    scale: Float? = null,
    onClick: () -> Unit = {},
    enableClick: Boolean = true,
    content: @Composable BoxScope.() -> Unit = {}
) {
    Box(
        modifier = modifier
            .run {
                scale?.let {
                    width(smallCardWidth * scale)
                    height(smallCardHeight * scale)
                } ?: this
            }
            .clip(RoundedCornerShape(8.dp))
            .background(
                color = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(0.5.dp)
            .background(
                color = accent.lerpCard(),
                shape = RoundedCornerShape(8.dp)
            ).run {
                if (enableClick) clickable(onClick = onClick) else this
            }
            .padding(8.dp),
        contentAlignment = Alignment.Center,
        content = content
    )

}

@Composable
fun Color.lerpCard(percent: Float = 0.25f): Color {
    return MaterialTheme.colorScheme.surface.let { s ->
        Color(
            red = s.red * (1 - percent) + (red * percent),
            green = s.green * (1 - percent) + (green * percent),
            blue = s.blue * (1 - percent) + (blue * percent),
        )
    }
}
