package com.dev_bayan_ibrahim.flashcards.data.model.card

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure

object CardSerializer : KSerializer<Card> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(
        "CardSerializer"
    ) {
//        element<Long>("id")
//        element("property", SomeSerializer.descriptor)
    }

    override fun deserialize(decoder: Decoder): Card {
        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {

                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("unexpected index $index")
                }
            }
        }
        return TODO("NOT IMPLEMENTED YET")
    }

    override fun serialize(
        encoder: Encoder,
        value: Card
    ) {
    }
}