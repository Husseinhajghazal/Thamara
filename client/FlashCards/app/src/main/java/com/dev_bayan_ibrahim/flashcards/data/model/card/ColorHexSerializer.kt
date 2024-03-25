package com.dev_bayan_ibrahim.flashcards.data.model.card

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object ColorHexSerializer : KSerializer<Int> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "ColorHexSerializer",
        PrimitiveKind.STRING
    )

    @OptIn(ExperimentalStdlibApi::class)
    override fun deserialize(decoder: Decoder): Int {
        val key = decoder.decodeString()
        return key.removePrefix("0X").removePrefix("0x").hexToInt(HexFormat.UpperCase)
    }

    override fun serialize(
        encoder: Encoder,
        value: Int
    ) {}
}