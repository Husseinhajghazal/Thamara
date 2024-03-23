package com.dev_bayan_ibrahim.flashcards.data.model.deck

import com.dev_bayan_ibrahim.flashcards.data.model.card.Card
import com.dev_bayan_ibrahim.flashcards.data.model.card.CardSerializer
import com.dev_bayan_ibrahim.flashcards.data.model.card.ColorHexSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.serializer

@OptIn(InternalSerializationApi::class)
object DeckSerializer : KSerializer<DeckHeader> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(
        "DeckHeaderSerializer"
    ) {
        element<Long>("id")
        element<Int>("version")
        element("tags", ListSerializer(String::class.serializer()).descriptor)

        element<String>("collection")
        element<String>("name")
        element<Int>("cards")

        element<String>("image_url")
        element("color", ColorHexSerializer.descriptor)
        element<Int>("level")

        element<Int>("rates_count")
        element<Float>("rate")
        element<Boolean>("shuffle")
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): DeckHeader {
        var id: Long = INVALID_ID
        var version: Int = 0
        val tags = mutableListOf<String>()

        var collection: String? = null
        var name: String = ""
        var cardsCount: Int = 0

        var pattern: String = "" // url for an image
        var color: Int = 0xFFFFFF

        var level: Int = 0
        var rates: Int = 0 // rates count
        var rate: Float = 0f // average rate

        var allowShuffle: Boolean = true

        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> id = decodeLongElement(descriptor, index)
                    1 -> version = decodeIntElement(descriptor, index)
                    2 -> tags.addAll(
                        decodeSerializableElement(
                            descriptor = descriptor,
                            index = index,
                            deserializer = ListSerializer(String::class.serializer())
                        )
                    )

                    3 -> collection = decodeNullableSerializableElement(
                        descriptor = descriptor,
                        index = index,
                        deserializer = String::class.serializer()
                    )

                    4 -> name = decodeStringElement(descriptor, index)
                    5 -> cardsCount = decodeIntElement(descriptor, index)

                    6 -> pattern = decodeStringElement(descriptor, index)
                    7 -> color = decodeSerializableElement(descriptor, index, ColorHexSerializer)
                    8 -> level = decodeIntElement(descriptor, index)
                    9 -> rates = decodeIntElement(descriptor, index)
                    10 -> rate = decodeNullableSerializableElement(descriptor, index, Float::class.serializer()) ?: 0f
                    11 -> allowShuffle = decodeBooleanElement(descriptor, index)

                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("unexpected index $index")
                }
            }
        }
        return DeckHeader(
            id = id,
            version = version,
            tags = tags,

            collection = collection,
            name = name,
            cardsCount = cardsCount,

            pattern = pattern,
            color = color,
            level = level,

            rates = rates,
            rate = rate,
            allowShuffle = allowShuffle,
        )
    }

    override fun serialize(
        encoder: Encoder,
        value: DeckHeader
    ) {
    }
}