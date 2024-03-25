package com.dev_bayan_ibrahim.flashcards.ui.app.util


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.util.ConnectivityStatus
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme
import kotlinx.coroutines.delay

private val heightAnimSpec = tween<Float>(200, easing = LinearOutSlowInEasing)
private val colorAnimSpec = tween<Color>(200, easing = LinearOutSlowInEasing)

@Composable
fun ConnectivityStatusBar(
    modifier: Modifier = Modifier,
    status: ConnectivityStatus
) {
    var init: ConnectivityStatus? by remember {
        mutableStateOf(status)
    }

    val heightAnimatable by remember {
        mutableStateOf(Animatable(0f))
    }
    val color by animateColorAsState(
        targetValue = when (status) {
            ConnectivityStatus.Available -> MaterialTheme.colorScheme.primary
            ConnectivityStatus.Unavailable -> MaterialTheme.colorScheme.error
        },
        label = "color",
        animationSpec = colorAnimSpec
    )

    val onColor by animateColorAsState(
        targetValue = when (status) {
            ConnectivityStatus.Available -> MaterialTheme.colorScheme.onPrimary
            ConnectivityStatus.Unavailable -> MaterialTheme.colorScheme.onError
        },
        label = "onColor"
    )

    LaunchedEffect(key1 = status) {
        if (status != init) {
            init = null
            heightAnimatable.animateTo(1f, animationSpec = heightAnimSpec)
            delay(1000)
            heightAnimatable.animateTo(0.1f, animationSpec = heightAnimSpec)
            delay(5000)
            heightAnimatable.animateTo(0f, animationSpec = heightAnimSpec)
        }
    }
// collecting state is not working
//    Row (
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
//        modifier = modifier
//            .fillMaxWidth()
//            .height(20.dp * heightAnimatable.value)
//            .drawBehind {
//                drawRect(color)
//            }
//            .padding(1.dp)
//    ) {
//        Icon(
//            painter = painterResource(id = status.iconRes),
//            contentDescription = null,
//            tint = onColor,
//        )
//        Text(
//            text = stringResource(id = status.hintRes),
//            style = MaterialTheme.typography.labelLarge,
//            color = onColor
//        )
//
//    }
}

@Preview(showBackground = true)
@Composable
private fun ConnectivityStatusBarPreviewLight() {
    FlashCardsTheme(darkTheme = false) {
        Surface(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.background,
        ) {
            var status: ConnectivityStatus by remember {
                mutableStateOf(ConnectivityStatus.Available)
            }

            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ConnectivityStatusBar(status = status)
                Button(
                    onClick = {
                        status = when (status) {
                            ConnectivityStatus.Available -> ConnectivityStatus.Unavailable
                            ConnectivityStatus.Unavailable -> ConnectivityStatus.Available
                        }
                    }
                ) {
                    Text(text = "Toggle")
                }
            }
        }
    }
}
