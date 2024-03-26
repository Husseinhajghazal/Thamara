package com.dev_bayan_ibrahim.flashcards.ui.screen.statistcs.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev_bayan_ibrahim.flashcards.data.model.statistics.TimeGroup
import com.dev_bayan_ibrahim.flashcards.data.model.statistics.TimeStatisticsItem
import com.dev_bayan_ibrahim.flashcards.data.repo.FlashRepo
import com.dev_bayan_ibrahim.flashcards.ui.util.fromEpochDays
import com.dev_bayan_ibrahim.flashcards.ui.util.groupByDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val repo: FlashRepo,

    ) : ViewModel() {
    val timedStatistics = TimeGroup.entries.map {
        repo.getTimeStatistics(it)
    }.run {
        combine(this) { flows ->
            val map = mutableMapOf<TimeGroup, TimeStatisticsItem>()
            flows.forEachIndexed { i, flow ->
                map[TimeGroup.entries[i]] = flow
            }
            map
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TimeGroup.entries.associateWith { TimeStatisticsItem() }
    )
    val state = StatisticsMutableUiState()

    fun initScreen() {
        viewModelScope.launch {
            val ranks = repo.getRankChangesStatistics().groupByDay {
                it.datetime
            }.mapNotNull {
                it.value.lastOrNull()
            }
            state.rankStatistics.run {
                clear()
                addAll(ranks)
            }

            val plays = repo.getPlaysStatistics().map {
                Triple(
                    it.deckPlay.datetime,
                    it.successPlays,
                    it.failedPlays,
                )
            }.groupByDay { it.first }.map { (key, value) ->
                Triple(
                    first = Instant.fromEpochDays(key),
                    second = value.sumOf { it.second },
                    third = value.sumOf { it.third },

                    )
            }
            state.playsStatistics.run {
                clear()
                addAll(plays)
            }
        }
    }
}