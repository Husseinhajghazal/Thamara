package com.dev_bayan_ibrahim.flashcards.data.model.statistics

import androidx.annotation.StringRes
import com.dev_bayan_ibrahim.flashcards.R


data class GeneralStatistics(
    val tags: Map<String, Int> = mapOf(), // count of decks in tags
    val accuracyAverage: Float = 0f, // percentage
    val totalDecksCount: Int = 0,
    val totalCardsCount: Int = 0,
)

data class TimeStatisticsItem(
    // experience
    val plays: Int = 0,

    // library
    val decksCount: Int = 0,
    val cardsCount: Int = 0,

    // performance
    val correctAnswers: Int = 0,
    val incorrectAnswers: Int = 0,
) {
    val answerAccuracyAverage: Float = correctAnswers * 100f / (correctAnswers + incorrectAnswers).coerceAtLeast(1)
}

enum class TimeGroup(@StringRes val nameRes: Int) {
    DAY(R.string.day),
    WEEK(R.string.week),
    MONTH(R.string.month);
}