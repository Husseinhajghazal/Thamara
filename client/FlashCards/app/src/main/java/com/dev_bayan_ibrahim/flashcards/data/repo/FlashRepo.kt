package com.dev_bayan_ibrahim.flashcards.data.repo

import com.dev_bayan_ibrahim.flashcards.data.model.deck.Deck
import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeader
import com.dev_bayan_ibrahim.flashcards.data.model.statistics.GeneralStatistics
import com.dev_bayan_ibrahim.flashcards.data.model.statistics.TimeGroup
import com.dev_bayan_ibrahim.flashcards.data.model.statistics.TimeStatisticsItem
import com.dev_bayan_ibrahim.flashcards.data.model.user.User
import com.dev_bayan_ibrahim.flashcards.data.model.user.UserRank
import com.dev_bayan_ibrahim.flashcards.data.util.DecksFilter
import com.dev_bayan_ibrahim.flashcards.data.util.DecksGroup
import com.dev_bayan_ibrahim.flashcards.data.util.DecksGroupType
import com.dev_bayan_ibrahim.flashcards.data.util.DecksOrder
import com.dev_bayan_ibrahim.flashcards.data.util.DownloadStatus
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.util.DecksDatabaseInfo
import kotlinx.coroutines.flow.Flow

interface FlashRepo {
    // Home
    fun getTotalPlaysCount(): Flow<Int>
    fun getUser(): Flow<User?>
    fun getGeneralStatistics(): Flow<GeneralStatistics>
    fun getTimeStatistics(group: TimeGroup): Flow<TimeStatisticsItem>
    suspend fun setUser (name: String, age: Int)

    fun getLibraryDecks(
        query: String,
        groupBy: DecksGroupType?,
        filterBy: DecksFilter?,
        orderBy: DecksOrder?,
    ): Flow<Map<DecksGroup, List<DeckHeader>>>

    suspend fun getDeckCards(id: Long): Deck

    suspend fun saveDeckResults(
        id: Long,
        cardsResults: Map<Long, Boolean> // answer status
    )

    suspend fun initializedDb()

    fun getDatabaseInfo(): Flow<DecksDatabaseInfo>
    suspend fun isFirstPlay(id: Long): Boolean
    suspend fun updateUserRank(newRank: UserRank)
    suspend fun downloadDeck(deck: Deck): Flow<DownloadStatus>
}