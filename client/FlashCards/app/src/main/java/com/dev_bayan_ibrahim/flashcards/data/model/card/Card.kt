package com.dev_bayan_ibrahim.flashcards.data.model.card

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Duration.Companion.days

@Entity(
    tableName = "card",
    foreignKeys = [
        ForeignKey(

        )
    ]
)
data class Card(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val deckId: Long,
    val question: String = "",
    val image: String = "",
    val answer: CardAnswer = CardAnswer.Info(1.days)
)