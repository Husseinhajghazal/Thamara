package com.dev_bayan_ibrahim.flashcards.data.util

import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeader

sealed interface DecksOrder {
    val asc: Boolean

    data class Name(override val asc: Boolean) : DecksOrder
    data class Rate(override val asc: Boolean) : DecksOrder
    data class Level(override val asc: Boolean) : DecksOrder

    fun applyOn(list: Iterable<DeckHeader>): List<DeckHeader> = when (this) {
        is Level -> if (asc) {
            list.sortedBy { it.level }
        } else {
            list.sortedByDescending { it.level }
        }

        is Name -> if (asc) {
            list.sortedBy { it.name }
        } else {
            list.sortedByDescending { it.name }
        }


        is Rate -> if (asc) {
            list.sortedBy { it.rate }
        } else {
            list.sortedByDescending { it.rate }
        }
    }
}

fun Iterable<DeckHeader>.applyDecksOrder(
    order: DecksOrder?,
): List<DeckHeader> = order?.applyOn(this) ?: toList()
