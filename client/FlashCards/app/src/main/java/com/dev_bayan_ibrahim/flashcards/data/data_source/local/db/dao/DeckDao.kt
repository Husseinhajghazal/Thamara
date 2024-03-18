package com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeader
import kotlinx.coroutines.flow.Flow

@Dao
interface DeckDao  {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDecks(decks: List<DeckHeader>)

    @Query("""
        select * from decks where id = :id
    """)
    suspend fun getDeck(id: Long): DeckHeader?

    @Query("""
        select tags from decks
    """)
    fun getTags(): Flow<List<String>>

    @Query("""
        select * from decks where name like :query
    """)
    fun getDecks(query: String): Flow<List<DeckHeader>>
    @Query("""
        select count(*) from decks
    """)
    fun getDecksCount(): Flow<Int>
    @Query("""
        select count(*) from decks where creation >= :startTimeStamp
    """)
    fun getDecksCountAfter(startTimeStamp: Long): Flow<Int>

    @Query("""
        select max(level) from decks 
    """)
    suspend fun getMaxDeckLevel(): Int?

    @Query("""
        select min(level) from decks 
    """)
    suspend fun getMinDeckLevel(): Int?
}
