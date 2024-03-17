package com.dev_bayan_ibrahim.flashcards.data.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.dev_bayan_ibrahim.flashcards.R

sealed class DecksGroup(
    val name: String,
    @StringRes
    val emptyNameRes: Int,
    @DrawableRes
    val icon: Int
) {
    data class Collection(
        val collection: String?
    ) : DecksGroup(
        name = collection ?: "",
        emptyNameRes = R.string.app_name,
        icon = R.drawable.ic_launcher_foreground
    )

    data class Tag(val tag: String) : DecksGroup(
        name = tag,
        emptyNameRes = R.string.app_name,
        icon = R.drawable.ic_launcher_foreground
    )

    data class Level(val level: Int) : DecksGroup(
        name = level.toString(),
        emptyNameRes = R.string.app_name,
        icon = R.drawable.ic_launcher_foreground
    )
    data object None : DecksGroup(
        name = "",
        emptyNameRes = R.string.app_name,
        icon = R.drawable.ic_launcher_foreground
    )
}
