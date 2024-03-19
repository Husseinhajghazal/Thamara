package com.dev_bayan_ibrahim.flashcards.data.data_source.local.storage

import com.dev_bayan_ibrahim.flashcards.data.model.deck.Deck
import com.dev_bayan_ibrahim.flashcards.data.util.DownloadStatus
import com.dev_bayan_ibrahim.flashcards.data.util.MutableDownloadStatus
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.contentLength
import io.ktor.http.isSuccess
import io.ktor.util.InternalAPI
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import java.io.OutputStream
import kotlin.coroutines.CoroutineContext

class FlashFileManagerImpl(
    filesDir: File,
    private val client: HttpClient,
    private val context: CoroutineContext
) : FlashFileManager {
    override fun saveDeck(
        deck: Deck,
    ): Flow<DownloadStatus> {
        val deckDir = ensureMakeDeckDir(deck.header.id)

        val deckPattern = getDeckPatternFile(deckDir, deck.header.pattern)
        val flows = mutableListOf<Flow<DownloadStatus>>()
        flows.add(
            downloadAndSaveFile(
                file = deckPattern.outputStream(),
                url = deck.header.pattern,
            )
        )
        flows.addAll(
            deck.cards.map { card ->
                val cardImage = getImageFile(deckDir, card.id, card.image)
                downloadAndSaveFile(
                    file = cardImage.outputStream(),
                    url = card.image,
                )
            }
        )
        return combine(
            flows
        ) { status ->
            status.reduce { i, j ->
                i + j
            }
        }.flowOn(context)
    }

    override suspend fun appendFilePathForImages(deck: Deck): Deck {
        var patternPath: String = deck.header.pattern
        val imagesPaths: MutableList<String> = deck.cards.map { it.image }.toMutableList()

        val deckDir = ensureMakeDeckDir(deck.header.id)

        val deckPattern = getDeckPatternFile(deckDir, deck.header.pattern)
        if (deckPattern.exists()) {
            patternPath = deckPattern.absolutePath
        }
        deck.cards.forEachIndexed { index, card ->
            val cardImage = getImageFile(deckDir, card.id, card.image)
            if (cardImage.exists()) {
                imagesPaths[index] = cardImage.absolutePath
            }
        }

        return deck.copy(
            header = deck.header.copy(
                pattern = patternPath
            ),
            cards = deck.cards.mapIndexed { index, card ->
                card.copy(image = imagesPaths[index])
            }
        )
    }

    private fun getDeckPatternFile(deckDir: File, pattern: String): File {
        return File(deckDir, "pattern${getExtensionFromUrlOrPng(pattern)}")
    }

    private fun getImageFile(deckDir: File, id: Long, image: String): File {
        val extension = getExtensionFromUrlOrPng(image)
        val name = "$id$extension"
        return File(deckDir, name)
    }

    override suspend fun deleteDeck(id: Long) {
        val deckDir = ensureMakeDeckDir(id)
        if (deckDir.exists()) {
            deckDir.deleteRecursively()
        }
    }

    override suspend fun deleteDecks(ids: List<Long>) = ids.forEach { id ->
        deleteDeck(id)
    }

    private fun ensureMakeDeckDir(deckId: Long): File {
        return File(
            decksDir,
            deckDirName(deckId)
        ).also {
            it.mkdirs()
        }
    }

    @OptIn(InternalAPI::class)
    private fun downloadAndSaveFile(
        file: OutputStream,
        url: String,
    ): Flow<DownloadStatus> = flow {

        val status = MutableDownloadStatus {
            currentCoroutineContext().cancel(null)
        }

        try {
            val response = client.get(url)

            val data = ByteArray(response.contentLength()!!.toInt())

            status.total = data.size.toLong()
            emit(status)

            var offset = 0

            do {
                val currentRead = response.content.readAvailable(data, offset, data.size)
                file.write(data, offset, currentRead)
                offset += currentRead

                status.progress = offset.toLong()
                emit(status)

            } while (currentRead > 0)


            if (response.status.isSuccess()) {
                file.use {
                    it.write(data)
                }
                status.success = true
            } else {
                status.error = response.status.run { value to description }
            }
        } catch (e: TimeoutCancellationException) {
            status.error = 408 to e.message
        } catch (t: Throwable) {
            status.error = null to t.message
        } finally {
            status.finished = true
            emit(status)
        }
    }.flowOn(context)


    private val decksDir = "${filesDir.absolutePath}/decks"
    private fun deckDirName(deckId: Long) = deckId.toString()
    private fun getExtensionFromUrlOrPng(
        url: String
    ): String = "\\.\\w+$".toRegex().find(url)?.value?.lowercase() ?: ".png"
}
