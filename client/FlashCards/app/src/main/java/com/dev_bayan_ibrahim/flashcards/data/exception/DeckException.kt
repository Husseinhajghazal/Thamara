package com.dev_bayan_ibrahim.flashcards.data.exception
open class DeckException(
    override val message: String?
): IllegalArgumentException(message)

open class DeckDeserializationException(
    override val message: String?
): DeckException(message)
