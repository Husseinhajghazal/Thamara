package com.dev_bayan_ibrahim.flashcards.data.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.dev_bayan_ibrahim.flashcards.R

enum class DecksGroupType (
    @StringRes
    override val label: Int,
    @DrawableRes
    override val icon: Int?,
): FlashSelectableItem {
    COLLECTION(R.string.collection, null),
    TAG(R.string.tag, null),
    LEVEL(R.string.level, null),
    NONE(R.string.none, null),
}
sealed class DecksGroup(
    val name: String,
    @StringRes
    val emptyNameRes: Int,
    val type: DecksGroupType,
) {
    data class Collection(
        val collection: String?
    ) : DecksGroup(
        name = collection ?: "",
        emptyNameRes = R.string.app_name,
        type = DecksGroupType.COLLECTION
    )

    data class Tag(val tag: String) : DecksGroup(
        name = tag,
        emptyNameRes = R.string.app_name,
        type = DecksGroupType.TAG
    )

    data class Level(val level: Int) : DecksGroup(
        name = level.toString(),
        emptyNameRes = R.string.app_name,
        type = DecksGroupType.LEVEL
    )

    data object None : DecksGroup(
        name = "",
        emptyNameRes = R.string.app_name,
        type = DecksGroupType.NONE
    )
}
