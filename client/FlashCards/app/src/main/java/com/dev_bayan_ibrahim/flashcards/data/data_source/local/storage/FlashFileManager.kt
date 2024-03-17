package com.dev_bayan_ibrahim.flashcards.data.data_source.local.storage

import com.dev_bayan_ibrahim.flashcards.data.model.deck.Deck
import com.dev_bayan_ibrahim.flashcards.data.util.MutableDownloadStatus


interface FlashFileManager {
    suspend fun saveDeck(
        deck: Deck,
        patternStatus: MutableDownloadStatus,
        imagesStatus: List<MutableDownloadStatus>
    )
    suspend fun appendFilePathForImages(deck: Deck): Deck
    suspend fun deleteDeck (deck: Deck) = deck.header.id?.let { deleteDeck(it) }
    suspend fun deleteDeck(id: Long)
}