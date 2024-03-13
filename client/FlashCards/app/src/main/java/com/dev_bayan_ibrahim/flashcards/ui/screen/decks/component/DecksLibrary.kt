package com.dev_bayan_ibrahim.flashcards.ui.screen.decks.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.dev_bayan_ibrahim.flashcards.data.model.deck.DeckHeader
import com.dev_bayan_ibrahim.flashcards.ui.constant.smallCardHeight
import com.dev_bayan_ibrahim.flashcards.ui.constant.smallCardWidth
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.util.DecksGroup

@Composable
fun PaginatedDecksList(
    modifier: Modifier = Modifier,
    decks: LazyPagingItems<DeckHeader>,
    onClickDeck: (Long) -> Unit,
) {
    DecksGrid(modifier = modifier) {
        countItem(decks.itemCount)
        repeat(decks.itemCount) {
            decks[it]?.let { deck ->
                deckItem(deck, onClickDeck)
            }
        }
    }
}

@Composable
fun DecksList(
    modifier: Modifier = Modifier,
    decksGroups: Map<DecksGroup, List<DeckHeader>>,
    onClickDeck: (Long) -> Unit,
) {
    val totalCount by remember(decksGroups) {
        derivedStateOf {
            decksGroups.map {(_, values) ->
                values.map { it.id }
            }.flatten().toSet().count()
        }
    }
    DecksGrid(
        modifier = modifier,
    ) {
        countItem(totalCount)
        decksGroups.forEach { (group, decks) ->
            groupTitle(group)
            deckItems(decks, onClickDeck)
        }
    }
}

@Composable
private fun DecksGrid(
    modifier: Modifier = Modifier,
    content: LazyGridScope.() -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.FixedSize(smallCardWidth),
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        content = content,
        contentPadding = PaddingValues(8.dp)
    )
}

private fun LazyGridScope.deckItem(
    deck: DeckHeader,
    onClickDeck: (Long) -> Unit,
) {
    item(
        key = deck.id,
        contentType = { "c" }
    ) {
        DeckItem(
            deckHeader = deck,
            onClick = { onClickDeck(deck.id) }
        )
    }
}

private fun LazyGridScope.deckItems(
    decks: List<DeckHeader>,
    onClickDeck: (Long) -> Unit,
) {
    items(
        items = decks,
        key = { it.id },
        contentType = { "c" }
    ) { deck ->
        DeckItem(
            deckHeader = deck,
            onClick = { onClickDeck(deck.id) }
        )
    }
}

private fun LazyGridScope.countItem(
    count: Int
) {
    item(
        contentType = "c",
        span = {
            GridItemSpan(maxLineSpan)
        },
    ) {
        Text(text = "$count Decks", style = MaterialTheme.typography.titleSmall)
    }
}

private fun LazyGridScope.groupTitle(
    group: DecksGroup,
) {
    item(
        key = group.name,
        contentType = "b",
        span = {
            GridItemSpan(maxLineSpan)
        },
    ) {
        Text(
            text = group.name.ifBlank { stringResource(id = group.emptyNameRes) },
            style = MaterialTheme.typography.bodyLarge
        )
    }
}