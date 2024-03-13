package com.dev_bayan_ibrahim.flashcards.ui.screen.decks.util

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
        val collection: String
    ): DecksGroup(
        name = collection,
        emptyNameRes = R.string.app_name,
        icon = R.drawable.ic_launcher_foreground
    )

}
