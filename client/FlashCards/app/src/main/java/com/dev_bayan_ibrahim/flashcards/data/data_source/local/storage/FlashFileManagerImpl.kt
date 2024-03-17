package com.dev_bayan_ibrahim.flashcards.data.data_source.local.storage

import com.dev_bayan_ibrahim.flashcards.data.model.deck.Deck
import com.dev_bayan_ibrahim.flashcards.data.util.MutableDownloadStatus
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.contentLength
import io.ktor.http.isSuccess
import io.ktor.util.InternalAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import java.io.File
import java.io.OutputStream

class FlashFileManagerImpl(
    filesDir: File,
    private val client: HttpClient
) : FlashFileManager {
    override suspend fun saveDeck(
        deck: Deck,
        patternStatus: MutableDownloadStatus,
        imagesStatus: List<MutableDownloadStatus>
    ) {
        val deckDir = ensureMakeDeckDir(deck.header.id!!)

        val deckPattern = getDeckPatternFile(deckDir, deck.header.pattern)
        downloadAndSaveFile(
            file = deckPattern.outputStream(),
            url = deck.header.pattern,
            downloadStatus = patternStatus
        )
        deck.cards.forEachIndexed { i, card ->
            val cardImage = getImageFile(deckDir, card.id!!, card.image)

            downloadAndSaveFile(
                file = cardImage.outputStream(),
                url = card.image,
                downloadStatus = imagesStatus.getOrNull(i)
            )
        }
    }

    override suspend fun appendFilePathForImages(deck: Deck): Deck {
        var patternPath: String =  deck.header.pattern
        val imagesPaths: MutableList<String> = deck.cards.map { it.image }.toMutableList()

        val deckDir = ensureMakeDeckDir(deck.header.id!!)

        val deckPattern = getDeckPatternFile(deckDir, deck.header.pattern)
        if (deckPattern.exists()) {
            patternPath = deckPattern.absolutePath
        }
        deck.cards.forEachIndexed { index, card ->
            val cardImage = getImageFile(deckDir, card.id!!, card.image)
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

    private suspend fun ensureMakeDeckDir(deckId: Long): File {
        return File(
            decksDir,
            deckDirName(deckId)
        ).also {
            it.mkdirs()
        }
    }

    @OptIn(InternalAPI::class)
    private suspend fun downloadAndSaveFile(
        file: OutputStream,
        url: String,
        downloadStatus: MutableDownloadStatus?
    ) {
        try {
            withContext(Dispatchers.IO) {
                val response = client.get(url)

                val data = ByteArray(response.contentLength()!!.toInt())

                downloadStatus?.total = data.size.toLong()
                downloadStatus?.progress = 0

                var offset = 0

                do {
                    val currentRead = response.content.readAvailable(data, offset, data.size)
                    file.write(data, offset, currentRead)
                    offset += currentRead
                    downloadStatus?.progress = offset.toLong()
                } while (currentRead > 0)


                if (response.status.isSuccess()) {
                    withContext(Dispatchers.IO) {
                        file.use {
                            it.write(data)
                        }
                        downloadStatus?.success = true
                    }
                } else {
                    downloadStatus?.error = response.status.run { value to description }
                }
            }
        } catch (e: TimeoutCancellationException) {
            downloadStatus?.error = (408 to e.message)
        } catch (t: Throwable) {
            downloadStatus?.error = (null to t.message)
        }
    }


    private val decksDir = "${filesDir.absolutePath}/decks"
    private fun deckDirName(deckId: Long) = deckId.toString()
    private fun getExtensionFromUrlOrPng(
        url: String
    ): String = "\\.\\w+$".toRegex().find(url)?.value?.lowercase() ?: ".png"
}
