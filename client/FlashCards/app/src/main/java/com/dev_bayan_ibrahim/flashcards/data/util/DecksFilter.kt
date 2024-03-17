package com.dev_bayan_ibrahim.flashcards.data.util

import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeader

data class DecksFilter (
    val tags: List<String> = emptyList(),
    val levels: IntRange? = null,
    val rate: ClosedFloatingPointRange<Float>? = null,

    ) {
    fun applyOn(list: Iterable<DeckHeader>): List<DeckHeader> = list.filter {
        (it.tags.any { it in tags } || it.tags.isEmpty())
                && levels?.contains(it.level) ?: true
                && rate?.contains(it.rate) ?: true
    }
}

fun Iterable<DeckHeader>.applyDecksFilter(
    filter: DecksFilter?
): List<DeckHeader> = filter?.applyOn(this) ?: toList()