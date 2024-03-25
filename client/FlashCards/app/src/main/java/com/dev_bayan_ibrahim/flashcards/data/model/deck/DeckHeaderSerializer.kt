package com.dev_bayan_ibrahim.flashcards.data.model.deck

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure

object DeckHeaderSerializer : KSerializer<DeckHeader> {
    override val descriptor: SerialDescriptor = buildDeckDescriptor(
        "DeckHeaderSerializer",
        true
    )
    override fun deserialize(decoder: Decoder): DeckHeader = deserializeDeck(
        decoder = decoder,
        descriptor = descriptor,
        headerOnly = true
    ).header

    override fun serialize(
        encoder: Encoder,
        value: DeckHeader
    ) { }
}

object CollectionSerializer: KSerializer<String> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(
        "CollectionSerializer"
    ) {
        element<Long>("id")
        element<Long>("name")
    }

    override fun deserialize(decoder: Decoder): String {
        var id: Long? = null
        var name: String? = null

        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> id = decodeLongElement(descriptor, index)
                    1 -> name = decodeStringElement(descriptor, index)

                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("unexpected index $index")
                }
            }

        }

        return name!!
    }

    override fun serialize(encoder: Encoder, value: String) {
        TODO("Not yet implemented")
    }

}