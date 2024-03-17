package com.dev_bayan_ibrahim.flashcards.data.model.deck

import com.dev_bayan_ibrahim.flashcards.data.model.card.Card

data class Deck(
//    @Embedded
    val header: DeckHeader = DeckHeader(),
//    @Relation(
//        parentColumn = "id",
//        entityColumn = "deckId"
//    )
    val cards: List<Card> = emptyList(),
)