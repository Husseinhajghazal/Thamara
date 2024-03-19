package com.dev_bayan_ibrahim.flashcards.data.model.user

import com.dev_bayan_ibrahim.flashcards.data.level_manager.FlashRankManager
import com.dev_bayan_ibrahim.flashcards.data.util.formatWithChar

data class UserRank(
    val rank: Int,
    val exp: Int,
) {
    companion object Companion {
        val Init = UserRank(0, 0)
    }

    val requiredExp = FlashRankManager.requiredExpOfRank(rank)
    val expPercent: Int = (100f * exp / requiredExp).toInt().coerceIn(0, 100)

    override fun toString(): String = "$rank - $expPercent% (${exp.toLong().formatWithChar()} - ${requiredExp.toLong().formatWithChar()})"
}

