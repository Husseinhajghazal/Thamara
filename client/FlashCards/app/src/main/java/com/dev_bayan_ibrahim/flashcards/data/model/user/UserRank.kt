package com.dev_bayan_ibrahim.flashcards.data.model.user

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.dev_bayan_ibrahim.flashcards.R
import com.dev_bayan_ibrahim.flashcards.data.rank_manager.FlashRankManager
import com.dev_bayan_ibrahim.flashcards.data.util.formatWithChar

data class UserRank(
    val rank: Int,
    val exp: Int,
) {
    companion object Companion {
        val top_rank = 18
        val min_rank = 0

        val ranks_range = min_rank..top_rank
        val Init = UserRank(min_rank, 0)
    }

    val requiredExp = FlashRankManager.requiredExpOfRank(rank)
    val expPercent: Int = (100f * exp / requiredExp).toInt().coerceIn(0, 100)

    override fun toString(): String = "$rank - $expPercent%"

    @Composable
    fun asString(): String = buildString {
        append(rank)
        append(" - ")
        append(expPercent)
        append("%(")
        append(
            stringResource(
                R.string.x_exp_of_y,
                exp.toLong().formatWithChar(),
                requiredExp.toLong().formatWithChar()
            )
        )
        append(")")
    }
}

