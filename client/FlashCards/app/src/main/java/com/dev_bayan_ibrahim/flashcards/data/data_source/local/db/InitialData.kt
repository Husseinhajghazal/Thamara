@file:Suppress("SpellCheckingInspection")

package com.dev_bayan_ibrahim.flashcards.data.data_source.local.db

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.dev_bayan_ibrahim.flashcards.data.model.card.Card
import com.dev_bayan_ibrahim.flashcards.data.model.card.CardAnswer
import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeader
import com.dev_bayan_ibrahim.flashcards.data.model.deck.split
import com.dev_bayan_ibrahim.flashcards.data.model.user.exportLinkOfKey
import kotlinx.datetime.Clock
import kotlin.random.Random
import kotlin.time.Duration.Companion.days

private val patterns by lazy {
    listOf(
        "1HiW96HMq-EPMLGvfsfS4Ow2VGZNTJGXt",
        "1IQN4SpSAEB9eaQ_2xn-tfsH0LZvmzlPU",
        "1IA1alnPWYp4DvWPCcC0tjR3s0XQREhvp",
        "1IRAKXG5u-sGiRdttd5DmpTxjFS0HqDxb",
        "1Ho7LfAJF24dGHgUkZoWDSUh9BZZOvstP",
        "1ISY0uu1w9co7gBYmUmUDt-em3B3i4Mql",
    ).map { it.exportLinkOfKey() }
}

private val beeKey by lazy { "1IRAKXG5u-sGiRdttd5DmpTxjFS0HqDxb" }
private val foxKey by lazy { "1ISY0uu1w9co7gBYmUmUDt-em3B3i4Mql" }
private val koalaKey by lazy { "1Ho7LfAJF24dGHgUkZoWDSUh9BZZOvstP" }
private val lionKey by lazy { "1IQN4SpSAEB9eaQ_2xn-tfsH0LZvmzlPU" }
private val turtleKey by lazy { "1IA1alnPWYp4DvWPCcC0tjR3s0XQREhvp" }

val qurranKey by lazy { "1Iy_Q7fOkE-tZEPs72naT9x2ey8swq-ZP" }

val decksInitialValue by lazy {
    listOf(
        DeckHeader(
            id = 1,
            tags = "children, en".split(),
            collection = "animals",
            name = "animal names",
            cardsCount = cardsInitialValues[1]?.count() ?: 0,
            pattern = patterns[0],
            color = Color.Red.toArgb(),
            level = 1,
            rates = 1,
            rate = 5f,
            creation = Clock.System.now()
        ),

        DeckHeader(
            id = 2,
            tags = "children, ar, qurran, islamic".split(),
            collection = "islam",
            name = "سورة الفاتحة",
            cardsCount = cardsInitialValues[2]?.count() ?: 0,
            pattern = patterns[1],
            color = Color.Green.toArgb(),
            level = 2,
            rates = 1,
            rate = 5f,
            creation = Clock.System.now()
        ),
    )
}

val cardsInitialValues by lazy {
    mapOf(
        1L to listOf(
            Card(
                id = 1,
                deckId = 1,
                question = "what is the name of this animal?",
                image = beeKey.exportLinkOfKey(),
                answer = CardAnswer.MultiChoice(1, "bee", "fox", "koala")
            ),
            Card(
                id = 2,
                deckId = 1,
                question = "what is the name of this animal?",
                image = foxKey.exportLinkOfKey(),
                answer = CardAnswer.MultiChoice(1, "fox", "koala", "lion")
            ),
            Card(
                id = 3,
                deckId = 1,
                question = "what is the name of this animal?",
                image = koalaKey.exportLinkOfKey(),
                answer = CardAnswer.MultiChoice(1, "koala", "turtle", "lion")
            ),
            Card(
                id = 4,
                deckId = 1,
                question = "what is the name of this animal?",
                image = turtleKey.exportLinkOfKey(),
                answer = CardAnswer.MultiChoice(3, "bee", "fox", "turtle")
            ),
            Card(
                id = 5,
                deckId = 1,
                question = "what is the name of this animal?",
                image = lionKey.exportLinkOfKey(),
                answer = CardAnswer.MultiChoice(3, "koala", "turtle", "lion")
            ),
        ),
        2L to listOf(
            Card(
                id = 6,
                deckId = 2,
                question = "ما هي السورة الأولى من سورة الفاتحة\nبسم الله الرحمن الرحيم",
                image = qurranKey.exportLinkOfKey(),
                answer = CardAnswer.Write("الحمد لله رب العالمين")
            ),
            Card(
                id = 7,
                deckId = 2,
                question = "أتمم السورة التالية، الحمد لله رب العالمين، ...",
                image = qurranKey.exportLinkOfKey(),
                answer = CardAnswer.Write("الرحمن الرحيم")
            ),
            Card(
                id = 8,
                deckId = 2,
                question = "أتمم السورة التالية، الرحمن الرحيم، ...",
                image = qurranKey.exportLinkOfKey(),
                answer = CardAnswer.Write("مالك يوم الدين")
            ),
            Card(
                id = 9,
                deckId = 2,
                question = "أتمم السورة التالية، مالك يوم الدين، ...",
                image = qurranKey.exportLinkOfKey(),
                answer = CardAnswer.Write("إياك نعبد وإياك نستعين")
            ),
            Card(
                id = 10,
                deckId = 2,
                question = "أتمم السورة التالية، إياك نعبد وإياك نستعين، ...",
                image = qurranKey.exportLinkOfKey(),
                answer = CardAnswer.Write("اهدنا الصراط المستقيم")
            ),
            Card(
                id = 11,
                deckId = 2,
                question = "أتمم السورة التالية، اهدنا الصراط المستقيم، ...",
                image = qurranKey.exportLinkOfKey(),
                answer = CardAnswer.Write("صراط الذين أنعمت عليهم")
            ),
            Card(
                id = 12,
                deckId = 2,
                question = "أتمم السورة التالية، صراط الذين أنعمت عليهم، ...",
                image = qurranKey.exportLinkOfKey(),
                answer = CardAnswer.Write("غير المغضوب عليهم")
            ),
            Card(
                id = 13,
                deckId = 2,
                question = "أتمم السورة التالية، غير المغضوب عليهم، ...",
                image = qurranKey.exportLinkOfKey(),
                answer = CardAnswer.Write("ولا الضالين")
            ),
        )
    )
}

val dummyTags by lazy {
    List(100) {
        "tag #$it"
    }
}
val dummyCollections by lazy {
    List(10) {
        "collection #$it"
    }
}

private fun randomColor(): Color {
    return Color (
        red = Random.nextInt(0, 255),
        green = Random.nextInt(0, 255),
        blue = Random.nextInt(0, 255),
    )
}
fun <T> List<T>.randomSublist(): List<T> {
    val size = Random.nextInt(0, count().dec())
    val indeces = (0 until count()).toMutableSet()
    return mutableListOf<T>().apply {
        repeat(size) {
            val index = indeces.random()
            indeces.remove(index)
            add(this@randomSublist[index])
        }
    }
}

fun generateLargeFakeDecks(
    decksCount: Int = 100,
    cardsForEach: Int = 100,
) = List(decksCount) {
    DeckHeader(
        id = it.inc().toLong(),
        tags = dummyTags.randomSublist(),
        collection = dummyCollections.random(),
        name = "deck - no.${it.inc()}",
        cardsCount = cardsForEach,
        pattern = patterns.random(),
        color = randomColor().toArgb(),
        level = Random.nextInt(1, 10),
        rates = Random.nextInt(0, 100_000),
        rate = Random.nextFloat() * 5,
        creation = Clock.System.now() - Random.nextInt(0, 100).days
    )
}

val dummyImages by lazy {
    listOf(
        beeKey,
        foxKey,
        koalaKey,
        lionKey,
        turtleKey,
        qurranKey,
    )
}

fun generateLargeFakeCards(
    decksCount: Int = 100,
    cardsForEach: Int = 100,
) = List(decksCount) { i1 ->
    val d = i1.inc().toLong()
    List(cardsForEach) { i2 ->
        val c = i2.inc().toLong()
        Card(
            id = cardsForEach * i1 + c,
            deckId = d,
            question = "question for card no.$c, of deck no.$d",
            image = dummyImages.random().exportLinkOfKey(),
            answer = when (c % 3) {
                0L -> CardAnswer.TrueFalse(true)
                1L -> CardAnswer.MultiChoice(
                    correctChoice = 1,
                    firstChoice = "choice no.1 for card no.$c, of deck no.$d",
                    secondChoice = "choice no.2 for card no.$c, of deck no.$d",
                    thirdChoice = "choice no.3 for card no.$c, of deck no.$d",
                )

                else -> CardAnswer.Write("answer for card no.$c, of deck no.$d")
            }
        )
    }
}.flatten()

fun generateLargeFakeCardsForDeck(
    d: Long,
    cardsForEach: Int = 100,
) = List(cardsForEach) { i2 ->
    val c = i2.inc().toLong()
    Card(
        id = cardsForEach * d + c,
        deckId = d.inc(),
        question = "question for card no.$c, of deck no.$d",
        image = dummyImages.random().exportLinkOfKey(),
        answer = when (c % 3) {
            0L -> CardAnswer.TrueFalse(true)
            1L -> CardAnswer.MultiChoice(
                correctChoice = 1,
                firstChoice = "choice no.1 for card no.$c, of deck no.$d",
                secondChoice = "choice no.2 for card no.$c, of deck no.$d",
                thirdChoice = "choice no.3 for card no.$c, of deck no.$d",
            )

            else -> CardAnswer.Write("answer for card no.$c, of deck no.$d")
        }
    )
}
