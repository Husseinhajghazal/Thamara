package com.dev_bayan_ibrahim.flashcards.data.repo

import androidx.compose.ui.text.intl.Locale
import androidx.paging.PagingData
import com.dev_bayan_ibrahim.flashcards.data.data_source.datastore.DataStoreManager
import com.dev_bayan_ibrahim.flashcards.data.data_source.init.cardsInitialValues
import com.dev_bayan_ibrahim.flashcards.data.data_source.init.decksInitialValue
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.dao.CardDao
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.dao.CardPlayDao
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.dao.DeckDao
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.dao.DeckPlayDao
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.dao.RankDao
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.database.FlashDatabase
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.storage.FlashFileManager
import com.dev_bayan_ibrahim.flashcards.data.data_source.remote.endpoint.Endpoint
import com.dev_bayan_ibrahim.flashcards.data.data_source.remote.endpoint.getEndpoint
import com.dev_bayan_ibrahim.flashcards.data.data_source.remote.io_field.input.InputField
import com.dev_bayan_ibrahim.flashcards.data.data_source.remote.io_field.output.OutputFieldCollectionSerializer
import com.dev_bayan_ibrahim.flashcards.data.data_source.remote.io_field.output.OutputFieldDeck
import com.dev_bayan_ibrahim.flashcards.data.data_source.remote.io_field.output.OutputFieldRate
import com.dev_bayan_ibrahim.flashcards.data.data_source.remote.io_field.output.OutputFieldSerializer
import com.dev_bayan_ibrahim.flashcards.data.data_source.remote.io_field.output.OutputFieldTagSerializer
import com.dev_bayan_ibrahim.flashcards.data.data_source.remote.paging.FlashPagingSource
import com.dev_bayan_ibrahim.flashcards.data.data_source.remote.request_builder.RequestBuilder
import com.dev_bayan_ibrahim.flashcards.data.data_source.remote.uri_decerator.UriDirector
import com.dev_bayan_ibrahim.flashcards.data.data_source.remote.util.BodyParam
import com.dev_bayan_ibrahim.flashcards.data.model.deck.Deck
import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeader
import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeaderSerializer
import com.dev_bayan_ibrahim.flashcards.data.model.play.CardPlay
import com.dev_bayan_ibrahim.flashcards.data.model.play.DeckPlay
import com.dev_bayan_ibrahim.flashcards.data.model.play.DeckWithCardsPlay
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
import com.dev_bayan_ibrahim.flashcards.data.util.MutableDownloadStatus
import com.dev_bayan_ibrahim.flashcards.data.util.applyDecksFilter
import com.dev_bayan_ibrahim.flashcards.data.util.applyDecksOrder
import com.dev_bayan_ibrahim.flashcards.data.util.shuffle
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.max_level
import com.dev_bayan_ibrahim.flashcards.ui.screen.app_design.min_level
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.util.DecksDatabaseInfo
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.time.Duration.Companion.days

@OptIn(InternalSerializationApi::class)
class FlashRepoImpl(
    private val db: FlashDatabase,
    private val preferences: DataStoreManager,
    private val fileManager: FlashFileManager,
    private val json: Json,
    private val director: UriDirector,
    private val client: HttpClient,
) : FlashRepo,
    DeckDao by db.getDeckDao(),
    CardDao by db.getCardDao(),
    DeckPlayDao by db.getDeckPlayDao(),
    CardPlayDao by db.getCardPlayDao(),
    RankDao by db.getRankDao() {


    private val endpoints: MutableMap<Endpoint, RequestBuilder> = mutableMapOf()

    private suspend fun <T> getEndpointRequest(
        endpoint: Endpoint,
        body: suspend (RequestBuilder) -> T
    ): T {
        val request = if (endpoints.containsKey(endpoint)) {
            endpoints[endpoint]!!
        } else {
            val e = getEndpoint(
                client = client,
                director = director,
                json = json,
                endpoint = endpoint
            )
            endpoints[endpoint] = e
            e
        }
        return body(request)
    }

    override fun getTotalPlaysCount(): Flow<Int> = getDeckPlaysCount()

    override fun getUser(): Flow<User?> = preferences.getUser()
    override suspend fun updateUserRank(newRank: UserRank) {
        registerRankChange(newRank)
        preferences.updateRank(newRank)
    }

    override fun getGeneralStatistics(): Flow<GeneralStatistics> {
        return combine(
            getAnswersOf(0, false),
            getAnswersOf(0, true),
            getFormattedTags().map {
                val tags = mutableMapOf<String?, Int>()
                it.forEach { tag ->
                    tags[tag] = 1 + (tags[tag] ?: 0)
                }

                val top10 = tags.toList().sortedByDescending {
                    it.second
                }.subList(
                    fromIndex = 0,
                    toIndex = minOf(0, tags.count())
                ).mapNotNull { it.first }.toSet()

                var others = 0
                tags.forEach { (tag, count) ->
                    if (!top10.contains(tag)) {
                        others += count
                    }
                }
                tags[null] = others

                tags
            },
            getDecksCount(),
            getCardsCount(),
        ) { correctAns, failedAns, tags, decks, cards ->
            GeneralStatistics(
                correctAnswers = correctAns,
                failedAnswers = failedAns,
                tags = tags,
                totalDecksCount = decks,
                totalCardsCount = cards
            )
        }
    }

    override fun getLibraryDecksIds(): Flow<Map<Long, Boolean>> = getDownloadedDecks().map {
        it.associate { header ->
            header.id to fileManager.imagesOffline(header.id, header.cardsCount)
        }
    }

    override suspend fun deleteDeckImages(id: Long) {
        fileManager.deleteDeck(id)
        updateDeckOfflineImages(id, false)
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

    override suspend fun List<DeckHeader>.applyDecksFilters(
        groupBy: DecksGroupType?,
        filterBy: DecksFilter?,
        orderBy: DecksOrder?
    ): Map<DecksGroup, List<DeckHeader>> = when (groupBy) {
        DecksGroupType.COLLECTION -> groupBy {
            it.collection
        }.mapKeys { (collection, _) ->
            DecksGroup.Collection(collection)
        }

        DecksGroupType.LEVEL -> groupBy {
            it.level
        }.mapKeys { (level, _) ->
            DecksGroup.Level(level)
        }

        DecksGroupType.TAG -> {
            val map = mutableMapOf<String, MutableList<DeckHeader>>()
            forEach { deck ->
                deck.tags.forEach { tag ->
                    map[tag]?.add(deck) ?: run { map[tag] = mutableListOf(deck) }
                }
            }
            map.mapKeys { (tag, _) ->
                DecksGroup.Tag(tag)
            }
        }

        else -> groupBy { DecksGroup.None }
    }.mapValues { (_, value) ->
        value.applyDecksFilter(filterBy)
            .applyDecksOrder(orderBy)
            .map {
                if (fileManager.imagesOffline(it.id, it.cardsCount)) {
                    it.copy(offlineImages = true)
                } else it
            }
    }.filter { (_, value) -> value.isNotEmpty() }


    override fun getLibraryDecks(
        query: String,
        groupBy: DecksGroupType?,
        filterBy: DecksFilter?,
        orderBy: DecksOrder?
    ): Flow<Map<DecksGroup, List<DeckHeader>>> =
        getDecks("%$query%").map { it.applyDecksFilters(groupBy, filterBy, orderBy) }


    override suspend fun getBrowseDecks(
        query: String,
        groupBy: DecksGroupType?,
        filterBy: DecksFilter?,
        orderBy: DecksOrder?
    ): Result<List<DeckHeader>> = getEndpointRequest(
        Endpoint.Deck
    ) {
        it.buildGetRequest(
            input = InputField(),
            deserializer = OutputFieldSerializer(DeckHeaderSerializer)
        ).execute().map {
            it.results
        }
    }

    override suspend fun getPaginatedBrowseDecks(
        query: String,
        groupBy: DecksGroupType?,
        filterBy: DecksFilter?,
        orderBy: DecksOrder?
    ): Flow<PagingData<DeckHeader>> = getEndpointRequest(
        Endpoint.Deck
    ) {
        FlashPagingSource.buildPagingDataFlow(
            input = InputField(),
            requestBuilder = it,
            serializer = DeckHeaderSerializer
        )
    }

    override suspend fun getDeckCards(id: Long): Deck {
        val header = getDeck(id)!! // todo, handle not found
        val cards = getCards(id).run {
            if (header.allowShuffle) {
                shuffle()
            } else {
                sortedBy { it.index }
            }
        }
        val deck = Deck(
            header = header,
            cards = cards
        )
        return fileManager.appendFilePathForImages(deck)
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

    override fun initializedDb() = flow {
        emit(false)
        fileManager.deleteDecks(getDownloadingDecks())
        deleteDownloadingDecks()
//        if (!preferences.initializedDb()) {
        if (false) {
//            val decksCount = 10
//            val cardsForEachDeck = 50
//            insertDecks(
//                generateLargeFakeDecks(decksCount, cardsForEachDeck)
//            )
//            repeat(decksCount) {
//                insertCards(
//                    generateLargeFakeCardsForDeck(it.toLong(), cardsForEachDeck)
//                )
//            }
            insertDecks(decksInitialValue)
            insertCards(cardsInitialValues.values.flatten())
            preferences.markAsInitializedDb()
        }
        emit(true)
    }

    override fun getDatabaseInfo(): Flow<DecksDatabaseInfo> = getCollections().combine(
        getFormattedTags()
    ) { collections, tags ->
        DecksDatabaseInfo(
            tags = tags.toSet(),
            collections = collections.toSet()
        )
    }

    override suspend fun isFirstPlay(id: Long): Boolean = checkDeckFirstPlay(id)

    private fun getFormattedTags() = getTags().map {
        it.mapNotNull {
            if (it.isNotBlank()) {
                it.split(", ")
            } else {
                null
            }
        }.flatten()
    }

    override suspend fun saveDeckToLibrary(
        deck: Deck,
        downloadImages: Boolean,
    ): Flow<DownloadStatus> {
        insertDeck(deck.header.copy(downloadInProgress = true, offlineData = true))
        insertCards(deck.cards)

        return if (downloadImages) {
            downloadDeckImages(deck)
        } else {
            finishDownloadDeck(deck.header.id)
            flow {
                val status = MutableDownloadStatus {}
                status.finished = true
                status.success = true
                emit(status)
            }
        }
    }


    override suspend fun downloadDeckImages(id: Long) = downloadDeckImages(getDeckCards(id))
    override suspend fun getRankChangesStatistics(): List<UserRank> = getRanksStatistics()
    override suspend fun getPlaysStatistics(): List<DeckWithCardsPlay> = getAllPlays()
    override suspend fun getLeveledDecksCount(): List<Pair<Int, Int>> {
        val levelsCount = (min_level..max_level).associateWith { 0 }.toMutableMap()
        getDownloadedDecks().first().groupBy { it.level }.forEach {
            levelsCount[it.key] = it.value.count()
        }
        return levelsCount.toList()
    }

    override suspend fun getAllTags(): Result<List<String>> = getEndpointRequest(
        Endpoint.Tag
    ) {
        it.buildGetRequest(InputField(), OutputFieldTagSerializer).execute().map { it.tags }
    }
    override suspend fun getAllCollections(): Result<List<String>> = getEndpointRequest(
        Endpoint.Collection
    ) {
        it.buildGetRequest(InputField(), OutputFieldCollectionSerializer).execute().map { it.collections }
    }

    override fun downloadDeckImages(deck: Deck) = fileManager.saveDeck(deck).map {
        it.also {
            if (it.finished) {
                if (it.success) {
                    finishDownloadDeck(deck.header.id)
                    updateDeckOfflineImages(deck.header.id, true)
                } else {
                    deleteDownloadingDecks()
                }
            }
        }
    }

    override suspend fun rateDeck(
        id: Long,
        rate: Int,
        deviceId: String
    ): Result<DeckHeader> = getEndpointRequest(
        Endpoint.Rate
    ) {
        it.buildPostRequest(
            InputField(
                bodyParams = listOf(
                    BodyParam.Text("deck_id", id.toString()),
                    BodyParam.Text("rate", rate.toString()),
                    BodyParam.Text("user_id", deviceId),
                )
            ),
            OutputFieldRate::class.serializer(),
        ).execute().map { it.updatedDeck }.updateDeckOnRate()
    }

    private suspend fun Result<DeckHeader>.updateDeckOnRate(): Result<DeckHeader> {
        getOrNull()?.let {
            updateDeckRate(id = it.id, rate = it.rate, rates = it.rates)
        }
        return this
    }

    override suspend fun getDeckInfo(id: Long): Result<Deck> = getEndpointRequest(
        Endpoint.Deck
    ) {
        it.buildGetRequest(id, OutputFieldDeck::class.serializer()).execute().map { it.deck }
    }
}