package com.dev_bayan_ibrahim.flashcards.data.model.deck

import com.dev_bayan_ibrahim.flashcards.data.model.card.Card

data class Deck(
    val header: DeckHeader = DeckHeader(),
    val cards: List<Card> = emptyList(),
)