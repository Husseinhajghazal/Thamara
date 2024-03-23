package com.dev_bayan_ibrahim.flashcards.data.data_source.remote.io_field.output

import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeader
import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OutputFieldRate(
    @SerialName("updated_deck")
    @Serializable(DeckSerializer::class) // todo change serializer
    val updatedDeck: DeckHeader
)