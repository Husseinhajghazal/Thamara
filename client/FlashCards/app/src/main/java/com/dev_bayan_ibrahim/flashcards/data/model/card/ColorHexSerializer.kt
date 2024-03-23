package com.dev_bayan_ibrahim.flashcards.data.model.card

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.random.Random

object ColorHexSerializer : KSerializer<Int> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "ColorHexSerializer",
        PrimitiveKind.STRING
    )

    @OptIn(ExperimentalStdlibApi::class)
    override fun deserialize(decoder: Decoder): Int {
        val key = decoder.decodeString()
        // todo format
        val argb = try {
            key.hexToInt(HexFormat.Default)
        } catch (e: Exception){
            Random.nextInt(0, 0XFF_FF_FF_FF.toInt())
        }
        return argb
    }

    override fun serialize(
        encoder: Encoder,
        value: Int
    ) {}
}