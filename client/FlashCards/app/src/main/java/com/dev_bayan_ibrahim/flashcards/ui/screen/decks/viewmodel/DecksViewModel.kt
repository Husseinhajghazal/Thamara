package com.dev_bayan_ibrahim.flashcards.ui.screen.decks.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeader
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.util.DecksGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class DecksViewModel @Inject constructor(

) : ViewModel(), DecksUiActions {
    val state = DecksMutableUiState()

    init {
        val colors = listOf(
            Color(0xFF000000),
            Color(0xFF444444),
            Color(0xFF888888),
            Color(0xFFCCCCCC),
            Color(0xFFFFFFFF),
            Color(0xFFFF0000),
            Color(0xFF00FF00),
            Color(0xFF0000FF),
            Color(0xFFFFFF00),
            Color(0xFF00FFFF),
            Color(0xFFFF00FF),
        )
        repeat(10) { g ->
            state.libraryDecks[DecksGroup.Collection("collection $g")] = List(
                colors.count()
            ) { d ->
                DeckHeader(
                    id = g.inc().times(colors.count()) + d.toLong(),
                    name = "deck $d",
                    collection = "collection $g",
                    colorAccent = colors[d],
                    cardsCount = colors.count(),
                    pattern = "https://drive.google.com/uc?export=download&id=1HiW96HMq-EPMLGvfsfS4Ow2VGZNTJGXt",
                    level = d,
                    rates = Random.nextInt(300),
                    rate = 1.2f
                )
            }
        }
    }

    override fun onSearchQueryChange(query: String) {
        state.query = query
    }

    override fun onClickDeck(id: Long) {

    }

    override fun onSearch() {
        TODO("Not yet implemented")
    }
}