package com.dev_bayan_ibrahim.flashcards.ui.util.animation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.graphics.TransformOrigin
import com.dev_bayan_ibrahim.flashcards.data.util.percentOf
import com.dev_bayan_ibrahim.flashcards.data.util.percentValueOfRange

val tweenFloat500 by lazy {
    tween<Float>(500, easing = FastOutSlowInEasing)
}
inline fun <reified T : Enum<T>> topNavEnterAnim(
    enum: Enum<T>
): EnterTransition {
    return fadeIn(
        animationSpec = tweenFloat500,
    ) + scaleIn(
        animationSpec = tweenFloat500,
        transformOrigin = enum.horizontalTransformOrigins()
    )
}

inline fun <reified T : Enum<T>> topNavExitAnim(
    enum: Enum<T>
): ExitTransition {
    return fadeOut(
        animationSpec = tweenFloat500,
    ) + scaleOut(
        animationSpec = tweenFloat500,
        transformOrigin = enum.horizontalTransformOrigins()
    )
}

fun navEnterAnim(): EnterTransition {
    return fadeIn(
        animationSpec = tweenFloat500
    ) + scaleIn(
        animationSpec = tweenFloat500,
        transformOrigin = TransformOrigin(0.5f, 0f)
    )
}
fun navExitAnim(): ExitTransition {
    return fadeOut(
        animationSpec = tweenFloat500
    ) + scaleOut(
        animationSpec = tweenFloat500,
        transformOrigin = TransformOrigin(0.5f, 0f)
    )
}


inline fun <reified T : Enum<T>> Enum<T>.horizontalBiasAlignment(): Alignment {
    val entries = enumValues<T>().count()
    val halesCount = entries * 2
    val halvesBefore = ordinal * 2 + 1
    val horizontalBias = (1..halesCount).percentOf(halvesBefore).percentValueOfRange(-1f..1f)
    return BiasAlignment(horizontalBias, 1f)
}

inline fun <reified T : Enum<T>> Enum<T>.horizontalTransformOrigins(): TransformOrigin {
    val entries = enumValues<T>().count()
    val halesCount = entries * 2
    val halvesBefore = ordinal * 2 + 1
    val xFraction = (0..halesCount).percentOf(halvesBefore)
    return TransformOrigin(xFraction, 1f)
}
