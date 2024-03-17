package com.dev_bayan_ibrahim.flashcards.data.model.card

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.serializer
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

sealed interface CardAnswer {
    /**
     * @param repeatRate in seconds
     */
    @Serializable
    data class Info(
        @Serializable(DurationSerializer::class)
        val repeatRate: Duration
    ) : CardAnswer


    @Serializable
    data class TrueFalse(val answer: Boolean) : CardAnswer

    @Serializable
    data class MultiChoice(
        val choices: List<String>,
        val correctChoice: String,
    ) : CardAnswer {
        constructor(
            correctChoice: Int,
            firstChoice: String,
            secondChoice: String,
            thirdChoice: String? = null,
            forthChoice: String? = null,
            fifthChoice: String? = null,
        ): this(
            choices = listOfNotNull(firstChoice, secondChoice, thirdChoice, forthChoice, fifthChoice),
            correctChoice = when(correctChoice.coerceIn(1..5)) {
                1 -> firstChoice; 2 -> secondChoice 3 -> thirdChoice!! 4 -> forthChoice!! else -> fifthChoice!!
            }
        )
    }

    @Serializable
    data class Write(val answer: String) : CardAnswer

    fun checkIfCorrect(answer: String): Boolean = when (this) {
        is Info -> true
        is MultiChoice -> answer == correctChoice
        is TrueFalse -> answer.toBooleanStrict() == this.answer
        is Write -> answer == this.answer
    }
}


@OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
object CardAnswerSerializer : KSerializer<CardAnswer> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(
        "CardAnswerSerializer"
    ) {
        element("info", CardAnswer.Info::class.serializer().descriptor)
        element("true_false", CardAnswer.TrueFalse::class.serializer().descriptor)
        element("multi_choice", CardAnswer.MultiChoice::class.serializer().descriptor)
        element("write", CardAnswer.Write::class.serializer().descriptor)
    }


    override fun deserialize(decoder: Decoder): CardAnswer {
        var info: CardAnswer.Info? = null
        var trueFalse: CardAnswer.TrueFalse? = null
        var multiChoice: CardAnswer.MultiChoice? = null
        var write: CardAnswer.Write? = null

        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {

                    0 -> info = decodeNullableSerializableElement(
                        descriptor = descriptor,
                        index = index,
                        deserializer = CardAnswer.Info::class.serializer()
                    )

                    1 -> trueFalse = decodeNullableSerializableElement(
                        descriptor = descriptor,
                        index = index,
                        deserializer = CardAnswer.TrueFalse::class.serializer()
                    )

                    2 -> multiChoice = decodeNullableSerializableElement(
                        descriptor = descriptor,
                        index = index,
                        deserializer = CardAnswer.MultiChoice::class.serializer()
                    )

                    3 -> write = decodeNullableSerializableElement(
                        descriptor = descriptor,
                        index = index,
                        deserializer = CardAnswer.Write::class.serializer()
                    )

                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("unexpected index $index")
                }
            }
        }
        return info ?: trueFalse ?: multiChoice ?: write ?: throw IllegalArgumentException("unexpected type")
    }


    override fun serialize(encoder: Encoder, value: CardAnswer) {
        encoder.encodeStructure(descriptor) {
            encodeNullableSerializableElement(
                descriptor = descriptor,
                index = 0,
                serializer = CardAnswer.Info::class.serializer(),
                value = value as? CardAnswer.Info
            )
            encodeNullableSerializableElement(
                descriptor = descriptor,
                index = 1,
                serializer = CardAnswer.TrueFalse::class.serializer(),
                value = value as? CardAnswer.TrueFalse
            )
            encodeNullableSerializableElement(
                descriptor = descriptor,
                index = 2,
                serializer = CardAnswer.MultiChoice::class.serializer(),
                value = value as? CardAnswer.MultiChoice
            )
            encodeNullableSerializableElement(
                descriptor = descriptor,
                index = 3,
                serializer = CardAnswer.Write::class.serializer(),
                value = value as? CardAnswer.Write
            )
        }
    }
}
object DurationSerializer: KSerializer<Duration> {
    override fun deserialize(decoder: Decoder): Duration {
        return decoder.decodeLong().toDuration(DurationUnit.MILLISECONDS)
    }

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "DurationSerializer",
        PrimitiveKind.LONG
    )

    override fun serialize(encoder: Encoder, value: Duration) {
        encoder.encodeLong(value.inWholeMicroseconds)
    }
}