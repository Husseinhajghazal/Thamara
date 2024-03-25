package com.dev_bayan_ibrahim.flashcards.ui.screen.decks


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.dev_bayan_ibrahim.flashcards.data.util.DownloadStatus
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.component.DecksList
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.component.DecksTopBar
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.component.DownloadDeckDialog
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.component.LibraryDeckDialog
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.component.PaginatedDecksList
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.util.DecksDatabaseInfo
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.util.DecksTab
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.viewmodel.DecksUiActions
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.viewmodel.DecksUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DecksScreen(
    modifier: Modifier = Modifier,
    state: DecksUiState,
    dbInfo: DecksDatabaseInfo,
    libraryDecksIds: Map<Long, Boolean>,
    downloadStatus: DownloadStatus?,
    actions: DecksUiActions,
) {
    val pagerState = rememberPagerState { DecksTab.entries.count() }
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        DecksTopBar(
            query = state.query,
            dbInfo = dbInfo,
            selected = DecksTab.entries[pagerState.currentPage],
            onQueryChange = actions::onSearchQueryChange,
            onSelectTab = {
                actions.onSelectTab(it)
                scope.launch {
                    pagerState.animateScrollToPage(it.ordinal)
                }
            },
            onSearch = actions::onSearch,
            dialogState = state.filterDialogState,
            dialogActions = actions,
        )
        val paginatedDecks = state.searchResults.collectAsLazyPagingItems()

        state.selectedDeck?.let { deckHeader ->
            if (deckHeader.id in libraryDecksIds) {
                LibraryDeckDialog(
                    show = true,
                    deck = deckHeader,
                    onDismiss = actions::onDismissSelectedDeck,
                    onPlay = actions::onPlayDeck
                )
            } else {
                DownloadDeckDialog(
                    show = true,
                    deck = deckHeader,
                    onDownload = actions::onDownloadDeck,
                    onCancel = actions::onCancelDownloadDeck,
                    downloadStatus = downloadStatus,
                )
            }
        }

        HorizontalPager(state = pagerState) { page ->
            when (DecksTab.entries[page]) {
                DecksTab.LIBRARY -> {
                    DecksList(
                        decksGroups = state.libraryDecks,
                        onClickDeck = actions::onClickDeck
                    )
                }

                DecksTab.BROWSE -> {
                    PaginatedDecksList(
                        modifier = Modifier.fillMaxWidth(),
                        decks = paginatedDecks,
                        libraryDecksIds = libraryDecksIds,
                        onClickDeck = actions::onClickDeck,
                    )
                }
            }
        }
    }
}

