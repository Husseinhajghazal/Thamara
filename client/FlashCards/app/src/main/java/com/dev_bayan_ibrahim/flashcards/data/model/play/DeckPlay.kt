package com.dev_bayan_ibrahim.flashcards.data.model.play

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.converter.InstantConverter
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@TypeConverters(InstantConverter::class)
@Entity("deck_play")
data class DeckPlay(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val deckId: Long,
    val datetime: Instant = Clock.System.now(),
)


/*
{
    "id": 1,
    "cardId": 123,
    "deckPlayId": 456,
    "failed": false
}
 */