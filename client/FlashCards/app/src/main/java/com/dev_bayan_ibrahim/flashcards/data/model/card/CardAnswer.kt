package com.dev_bayan_ibrahim.flashcards.data.model.card

import kotlin.time.Duration

sealed interface CardAnswer {
    /**
     * @param repeatRate in seconds
     */
    data class Info(val repeatRate: Duration) : CardAnswer
    data class TrueFalse(val answer: Boolean) : CardAnswer
    data class MultiChoice(val choices: List<String>) : CardAnswer
    data class Write(val answer: String) : CardAnswer
}