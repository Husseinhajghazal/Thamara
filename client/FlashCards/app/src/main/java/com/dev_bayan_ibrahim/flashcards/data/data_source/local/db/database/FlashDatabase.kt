package com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.dao.CardDao
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.dao.CardPlayDao
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.dao.DeckDao
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.dao.DeckPlayDao
import com.dev_bayan_ibrahim.flashcards.data.model.card.Card
import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeader
import com.dev_bayan_ibrahim.flashcards.data.model.play.CardPlay
import com.dev_bayan_ibrahim.flashcards.data.model.play.DeckPlay

@Database(
    entities = [DeckHeader::class, Card::class, DeckPlay::class, CardPlay::class],
    version = 3,
    exportSchema = false,
)
abstract class FlashDatabase : RoomDatabase() {
    abstract fun getCardDao(): CardDao
    abstract fun getDeckDao(): DeckDao
    abstract fun getDeckPlayDao(): DeckPlayDao
    abstract fun getCardPlayDao(): CardPlayDao
}