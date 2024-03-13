package com.dev_bayan_ibrahim.flashcards.data.model.deck

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deck")
data class DeckHeader(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val version: Int = 1,
    val tags: List<String> = emptyList(),
    val collection: String? = null,
    val name: String = "",
    val cardsCount: Int = 0,
    val pattern: String = "", // url for an image
    val colorAccent: Color = Color(0xFFFFFFFF),
    val level: Int = 0,
    val rates: Int = 0, // rates count
    val rate: Float = 0f, // average rate
)