package com.dev_bayan_ibrahim.flashcards.data.repo

import androidx.compose.ui.text.intl.Locale
import com.dev_bayan_ibrahim.flashcards.data.data_source.datastore.DataStoreManager
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.dao.CardDao
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.dao.CardPlayDao
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.dao.DeckDao
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.dao.DeckPlayDao
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.database.FlashDatabase
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.generateLargeFakeCardsForDeck
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.generateLargeFakeDecks
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.storage.FlashFileManager
import com.dev_bayan_ibrahim.flashcards.data.model.deck.Deck
import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeader
import com.dev_bayan_ibrahim.flashcards.data.model.play.CardPlay
import com.dev_bayan_ibrahim.flashcards.data.model.play.DeckPlay
import com.dev_bayan_ibrahim.flashcards.data.model.statistics.GeneralStatistics
import com.dev_bayan_ibrahim.flashcards.data.model.statistics.TimeGroup
import com.dev_bayan_ibrahim.flashcards.data.model.statistics.TimeGroup.DAY
import com.dev_bayan_ibrahim.flashcards.data.model.statistics.TimeGroup.MONTH
import com.dev_bayan_ibrahim.flashcards.data.model.statistics.TimeGroup.WEEK
import com.dev_bayan_ibrahim.flashcards.data.model.statistics.TimeStatisticsItem
import com.dev_bayan_ibrahim.flashcards.data.model.user.User
import com.dev_bayan_ibrahim.flashcards.data.model.user.UserRank
import com.dev_bayan_ibrahim.flashcards.data.util.DecksFilter
import com.dev_bayan_ibrahim.flashcards.data.util.DecksGroup
import com.dev_bayan_ibrahim.flashcards.data.util.DecksGroupType
import com.dev_bayan_ibrahim.flashcards.data.util.DecksOrder
import com.dev_bayan_ibrahim.flashcards.data.util.DownloadStatus
import com.dev_bayan_ibrahim.flashcards.data.util.applyDecksFilter
import com.dev_bayan_ibrahim.flashcards.data.util.applyDecksOrder
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.util.DecksDatabaseInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.days

class FlashRepoImpl(
    private val db: FlashDatabase,
    private val preferences: DataStoreManager,
    private val fileManager: FlashFileManager,
) : FlashRepo,
    DeckDao by db.getDeckDao(),
    CardDao by db.getCardDao(),
    DeckPlayDao by db.getDeckPlayDao(),
    CardPlayDao by db.getCardPlayDao() {
    override fun getTotalPlaysCount(): Flow<Int> = getDeckPlaysCount()

    override fun getUser(): Flow<User?> = preferences.getUser()
    override suspend fun updateUserRank(newRank: UserRank) = preferences.updateRank(newRank)

    override fun getGeneralStatistics(): Flow<GeneralStatistics> {
        return combine(
            getCardsAccuracyAverage(),
            getFormattedTags().map {
                val tags = mutableMapOf<String, Int>()
                it.forEach { deckTags ->
                    deckTags.split(", ").forEach { tag ->
                        tags[tag] = 1 + (tags[tag] ?: 0)
                    }
                }
                tags
            },
            getDecksCount(),
            getCardsCount(),
        ) { avg, tags, decks, cards ->
            GeneralStatistics(
                accuracyAverage = avg,
                tags = tags,
                totalDecksCount = decks,
                totalCardsCount = cards
            )
        }
    }

    override fun getTimeStatistics(
        group: TimeGroup
    ): Flow<TimeStatisticsItem> {
        Locale
        val timeZone = TimeZone.currentSystemDefault()
        val now = Clock.System.now().toLocalDateTime(timeZone)
        val startTimeStamp = when (group) {
            DAY -> now.date.atTime(0, 0).toInstant(timeZone)
            WEEK -> {
                val sat = DayOfWeek.SATURDAY
                val dates = (now.date.dayOfWeek.value.plus(7) - sat.value).mod(7)
                (now.date.atTime(0, 0).toInstant(timeZone) - dates.days)
            }

            MONTH -> java.time.LocalDate.of(
                now.date.year,
                now.date.month,
                1
            ).atTime(0, 0)
                .toKotlinLocalDateTime()
                .toInstant(timeZone)
        }.toEpochMilliseconds()

        return combine(
            getPlaysCountAfter(startTimeStamp),
            getDecksCountAfter(startTimeStamp),
            getCardsCountAfter(startTimeStamp),
            getAnswersOf(startTimeStamp, false),
            getAnswersOf(startTimeStamp, true)
        ) { plays, decks, cards, correct, failed ->
            TimeStatisticsItem(
                plays = plays,
                decksCount = decks,
                cardsCount = cards,
                correctAnswers = correct,
                incorrectAnswers = failed
            )
        }
    }

    override suspend fun setUser(name: String, age: Int) = preferences.setUser(name, age)

    override fun getLibraryDecks(
        query: String,
        groupBy: DecksGroupType?,
        filterBy: DecksFilter?,
        orderBy: DecksOrder?
    ): Flow<Map<DecksGroup, List<DeckHeader>>> = when (groupBy) {
        DecksGroupType.COLLECTION -> getDecks("%$query%").map {
            it.groupBy {
                it.collection
            }.mapKeys { (collection, _) ->
                DecksGroup.Collection(collection)
            }
        }

        DecksGroupType.LEVEL -> getDecks("%$query%").map {
            it.groupBy {
                it.level
            }.mapKeys { (level, _) ->
                DecksGroup.Level(level)
            }
        }

        DecksGroupType.TAG -> getDecks("%$query%").map {
            val map = mutableMapOf<String, MutableList<DeckHeader>>()
            it.forEach { deck ->
                deck.tags.forEach { tag ->
                    map[tag]?.add(deck) ?: run { map[tag] = mutableListOf(deck) }
                }
            }
            map.mapKeys { (tag, _) ->
                DecksGroup.Tag(tag)
            }
        }

        else -> getDecks("%$query%").map {
            it.groupBy { DecksGroup.None }
        }
    }.map { map ->
        map.mapValues { (_, value) ->
            value.applyDecksFilter(filterBy).applyDecksOrder(orderBy)
        }.filter { (_, value) -> value.isNotEmpty() }
    }

    override suspend fun getDeckCards(id: Long): Deck {
        val header = getDeck(id)!! // todo, handle not found
        val cards = getCards(id)
        return Deck(header = header, cards = cards)
    }

    override suspend fun saveDeckResults(
        id: Long,
        cardsResults: Map<Long, Boolean>
    ) {
        val deckPlayId = insertDeckPlay(DeckPlay(deckId = id))
        val cards = cardsResults.map { (id, correct) ->
            CardPlay(
                cardId = id,
                deckPlayId = deckPlayId,
                failed = !correct,
            )
        }
        insertAllCards(cards)
    }

    override suspend fun initializedDb() {
        fileManager.deleteDecks(getDownloadingDecks())
        deleteDownloadingDecks()
        if (!preferences.initializedDb()) {
            val decksCount = 10
            val cardsForEachDeck = 5
            insertDecks(
                generateLargeFakeDecks(decksCount, cardsForEachDeck)
            )
            repeat(decksCount) {
                insertCards(
                    generateLargeFakeCardsForDeck(it.toLong(), cardsForEachDeck)
                )
            }
//            insertDecks(decksInitialValue)
//            insertCards(cardsInitialValues.values.flatten())
            preferences.markAsInitializedDb()
        }
    }

    override fun getDatabaseInfo(): Flow<DecksDatabaseInfo> = getFormattedTags().map { tags ->
        DecksDatabaseInfo(
            allTags = tags.toSet(),
        )
    }

    override suspend fun isFirstPlay(id: Long): Boolean = checkDeckFirstPlay(id)

    private fun getFormattedTags() = getTags().map { it.map { it.split(", ") }.flatten() }

    override suspend fun downloadDeck(deck: Deck): Flow<DownloadStatus> {
        insertDeck(deck.header.copy(downloadInProgress = true))
        insertCards(deck.cards)
        return fileManager.saveDeck(deck).map {
            it.also {
                if (it.finished) {
                    if (it.success) {
                        finishDownloadDeck(deck.header.id)
                    } else {
                        deleteDownloadingDecks()
                    }
                }
            }
        }
    }
}