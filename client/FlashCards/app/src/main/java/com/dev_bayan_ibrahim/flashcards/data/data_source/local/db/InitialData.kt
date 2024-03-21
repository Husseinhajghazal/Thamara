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
        "1Ij89Wsaat9VBDwT2L5YMl4MUop_4rsBn",
        "1IoGs1uhtweqZo81k6eY341qwir36H2s_",
        "1IpOHfgCRAsL70QtBKsqrPyJb1V3qZhH-",
        "1IrRJ1KWndLf1On_CehBW25_wnf332xr3",
        "1ItKh6GZK17yauixqAlz3vAe344KjfnhI",
    ).map { it.exportLinkOfKey() }
}

private val beeKey by lazy { "1IRAKXG5u-sGiRdttd5DmpTxjFS0HqDxb" }
private val foxKey by lazy { "1ISY0uu1w9co7gBYmUmUDt-em3B3i4Mql" }
private val koalaKey by lazy { "1Ho7LfAJF24dGHgUkZoWDSUh9BZZOvstP" }
private val lionKey by lazy { "1IQN4SpSAEB9eaQ_2xn-tfsH0LZvmzlPU" }
private val turtleKey by lazy { "1IA1alnPWYp4DvWPCcC0tjR3s0XQREhvp" }

val qurranKey by lazy { "1Iy_Q7fOkE-tZEPs72naT9x2ey8swq-ZP" }

val salatKey by lazy { "1JuY9oh2IP8ZR10VuzAqqquSV186D3hMr" }
val paryingKey by lazy { "1JUXSfXfiYIugJA-TJFJwCFjPg-BkPGGv" }
val mosqueKey by lazy { "1Jq5hzmmLpT14oiuWa-31ss_biRK42kFf" }
val pulbitKey by lazy { "1JkOBUXs9S0sgywtuLaV75LJ9R6NIbXge" }
val beadsKey by lazy { "1Jhqatdr3yhm1yEFKotoTtcSZGQv7Zi-E" }
val kaabaKey by lazy { "1Jg70DvPMie2n7TB0VI1-7Sq8RZdbiQ7L" }
val moonKey by lazy { "1K3jDFpV7Y-mm9j4pf6wQVoVxVPwA-NW7" }

val decksInitialValue by lazy {
    listOf(
        DeckHeader(
            id = 1,
            tags = "أطفال, انجليزية".split(),
            collection = "حيوانات",
            name = "اسماء الحيوانات",
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
            tags = "أطفال, قرآن, اسلام".split(),
            collection = "إسلام",
            name = "سورة الفاتحة",
            cardsCount = cardsInitialValues[2]?.count() ?: 0,
            pattern = patterns[1],
            color = Color.Green.toArgb(),
            level = 2,
            rates = 1,
            rate = 5f,
            creation = Clock.System.now(),
            allowShuffle = false,
        ),
        DeckHeader(
            id = 3,
            tags = "مجتمع إسلامي, أساسي".split(),
            collection = "إسلام",
            name = "أساسيات الإسلام",
            cardsCount = cardsInitialValues[3]?.count() ?: 0,
            pattern = patterns[2],
            color = Color.Yellow.toArgb(),
            level = 2,
            rates = 1,
            rate = 5f,
            creation = Clock.System.now(),
        ),
    )
}

val cardsInitialValues by lazy {
    mapOf(
        // first is the deck id, must be increased on every map item
        1L to listOf(
            Card(
                id = 1,
                deckId = 1,
                index = 0,
                question = "ما اسم هذا الحيوان؟",
                image = beeKey.exportLinkOfKey(),
                answer = CardAnswer.MultiChoice(1, "bee", "fox", "koala")
            ),
            Card(
                id = 2,
                deckId = 1,
                index = 1,
                question = "ما اسم هذا الحيوان؟",
                image = foxKey.exportLinkOfKey(),
                answer = CardAnswer.MultiChoice(1, "fox", "koala", "lion")
            ),
            Card(
                id = 3,
                deckId = 1,
                index = 2,
                question = "ما اسم هذا الحيوان؟",
                image = koalaKey.exportLinkOfKey(),
                answer = CardAnswer.MultiChoice(1, "koala", "turtle", "lion")
            ),
            Card(
                id = 4,
                deckId = 1,
                index = 3,
                question = "ما اسم هذا الحيوان؟",
                image = turtleKey.exportLinkOfKey(),
                answer = CardAnswer.MultiChoice(3, "bee", "fox", "turtle")
            ),
            Card(
                id = 5,
                deckId = 1,
                index = 4,
                question = "ما اسم هذا الحيوان؟",
                image = lionKey.exportLinkOfKey(),
                answer = CardAnswer.MultiChoice(3, "koala", "turtle", "lion")
            ),
        ),
        2L to listOf(
            Card(
                id = 6,
                deckId = 2,
                index = 0,
                question = "ما هي السورة الأولى من سورة الفاتحة\nبسم الله الرحمن الرحيم",
                image = qurranKey.exportLinkOfKey(),
                answer = CardAnswer.Write("الحمد لله رب العالمين")
            ),
            Card(
                id = 7,
                deckId = 2,
                index = 1,
                question = "أتمم السورة التالية، الحمد لله رب العالمين، ...",
                image = qurranKey.exportLinkOfKey(),
                answer = CardAnswer.Write("الرحمن الرحيم")
            ),
            Card(
                id = 8,
                deckId = 2,
                index = 2,
                question = "أتمم السورة التالية، الرحمن الرحيم، ...",
                image = qurranKey.exportLinkOfKey(),
                answer = CardAnswer.Write("مالك يوم الدين")
            ),
            Card(
                id = 9,
                deckId = 2,
                index = 3,
                question = "أتمم السورة التالية، مالك يوم الدين، ...",
                image = qurranKey.exportLinkOfKey(),
                answer = CardAnswer.Write("إياك نعبد وإياك نستعين")
            ),
            Card(
                id = 10,
                deckId = 2,
                index = 4,
                question = "أتمم السورة التالية، إياك نعبد وإياك نستعين، ...",
                image = qurranKey.exportLinkOfKey(),
                answer = CardAnswer.Write("اهدنا الصراط المستقيم")
            ),
            Card(
                id = 11,
                deckId = 2,
                index = 5,
                question = "أتمم السورة التالية، اهدنا الصراط المستقيم، ...",
                image = qurranKey.exportLinkOfKey(),
                answer = CardAnswer.Write("صراط الذين أنعمت عليهم")
            ),
            Card(
                id = 12,
                deckId = 2,
                index = 6,
                question = "أتمم السورة التالية، صراط الذين أنعمت عليهم، ...",
                image = qurranKey.exportLinkOfKey(),
                answer = CardAnswer.Write("غير المغضوب عليهم")
            ),
            Card(
                id = 13,
                deckId = 2,
                index = 7,
                question = "أتمم السورة التالية، غير المغضوب عليهم، ...",
                image = qurranKey.exportLinkOfKey(),
                answer = CardAnswer.Write("ولا الضالين")
            ),
        ),
        3L to listOf(
            Card(
                id = 14,
                deckId = 3,
                index = 0,
                image = qurranKey.exportLinkOfKey(),
                question = "ما هي أول سورة في القرآن الكريم؟",
                answer = CardAnswer.MultiChoice(
                    correctChoice = 1,
                    firstChoice = "الفاتحة",
                    secondChoice = "البقرة",
                    thirdChoice = "آل عمران"
                )
            ),
            Card(
                id = 15,
                deckId = 3,
                image = paryingKey.exportLinkOfKey(),
                index = 1,
                question = "كم عدد الركعات في صلاة الفجر؟",
                answer = CardAnswer.MultiChoice(
                    correctChoice = 1,
                    firstChoice = "2",
                    secondChoice = "4",
                    thirdChoice = "6"
                )
            ),
            Card(
                id = 16,
                deckId = 3,
                index = 2,
                image = moonKey.exportLinkOfKey(),
                question = "من هو خاتم النبيين؟",
                answer = CardAnswer.MultiChoice(
                    correctChoice = 1,
                    firstChoice = "النبي محمد صلى الله عليه وسلم",
                    secondChoice = "النبي موسى عليه السلام",
                    thirdChoice = "النبي عيسى عليه السلام"
                )
            ),
            Card(
                id = 17,
                deckId = 3,
                index = 3,
                image = mosqueKey.exportLinkOfKey(),
                question = "ما هو عدد اركان الإسلام؟",
                answer = CardAnswer.MultiChoice(
                    correctChoice = 1,
                    firstChoice = "خمسة",
                    secondChoice = "ثلاثة",
                    thirdChoice = "سبعة"
                )
            ),
            Card(
                id = 18,
                deckId = 3,
                index = 4,
                image = kaabaKey.exportLinkOfKey(),
                question = "من هو نبي الله الخليل؟",
                answer = CardAnswer.MultiChoice(
                    correctChoice = 1,
                    firstChoice = "النبي إبراهيم عليه السلام",
                    secondChoice = "النبي موسى عليه السلام",
                    thirdChoice = "النبي عيسى عليه السلام"
                ),
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
    return Color(
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
        beadsKey,
        beeKey,
        foxKey,
        kaabaKey,
        koalaKey,
        lionKey,
        moonKey,
        mosqueKey,
        paryingKey,
        pulbitKey,
        qurranKey,
        salatKey,
        turtleKey,
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
            index = i2,
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
        index = i2,
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
