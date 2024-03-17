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
                deckId = 1,
                question = "what is the name of this animal?",
                image = beeKey.exportLinkOfKey(),
                answer = CardAnswer.MultiChoice(1, "bee", "fox", "koala")
            ),
            Card(
                deckId = 1,
                question = "what is the name of this animal?",
                image = foxKey.exportLinkOfKey(),
                answer = CardAnswer.MultiChoice(1, "fox", "koala", "lion")
            ),
            Card(
                deckId = 1,
                question = "what is the name of this animal?",
                image = koalaKey.exportLinkOfKey(),
                answer = CardAnswer.MultiChoice(1, "koala", "turtle", "lion")
            ),
            Card(
                deckId = 1,
                question = "what is the name of this animal?",
                image = turtleKey.exportLinkOfKey(),
                answer = CardAnswer.MultiChoice(3, "bee", "fox", "turtle")
            ),
            Card(
                deckId = 1,
                question = "what is the name of this animal?",
                image = lionKey.exportLinkOfKey(),
                answer = CardAnswer.MultiChoice(3, "koala", "turtle", "lion")
            ),
        ),
        2L to listOf(
            Card(
                deckId = 2,
                question = "ما هي السورة الأولى من سورة الفاتحة\nبسم الله الرحمن الرحيم",
                image = qurranKey.exportLinkOfKey(),
                answer = CardAnswer.Write("الحمد لله رب العالمين")
            ),
            Card(
                deckId = 2,
                question = "أتمم السورة التالية، الحمد لله رب العالمين، ...",
                image = qurranKey.exportLinkOfKey(),
                answer = CardAnswer.Write("الرحمن الرحيم")
            ),
            Card(
                deckId = 2,
                question = "أتمم السورة التالية، الرحمن الرحيم، ...",
                image = qurranKey.exportLinkOfKey(),
                answer = CardAnswer.Write("مالك يوم الدين")
            ),
            Card(
                deckId = 2,
                question = "أتمم السورة التالية، مالك يوم الدين، ...",
                image = qurranKey.exportLinkOfKey(),
                answer = CardAnswer.Write("إياك نعبد وإياك نستعين")
            ),
            Card(
                deckId = 2,
                question = "أتمم السورة التالية، إياك نعبد وإياك نستعين، ...",
                image = qurranKey.exportLinkOfKey(),
                answer = CardAnswer.Write("اهدنا الصراط المستقيم")
            ),
            Card(
                deckId = 2,
                question = "أتمم السورة التالية، اهدنا الصراط المستقيم، ...",
                image = qurranKey.exportLinkOfKey(),
                answer = CardAnswer.Write("صراط الذين أنعمت عليهم")
            ),
            Card(
                deckId = 2,
                question = "أتمم السورة التالية، صراط الذين أنعمت عليهم، ...",
                image = qurranKey.exportLinkOfKey(),
                answer = CardAnswer.Write("غير المغضوب عليهم")
            ),
            Card(
                deckId = 2,
                question = "أتمم السورة التالية، غير المغضوب عليهم، ...",
                image = qurranKey.exportLinkOfKey(),
                answer = CardAnswer.Write("ولا الضالين")
            ),
        )
    )
}
