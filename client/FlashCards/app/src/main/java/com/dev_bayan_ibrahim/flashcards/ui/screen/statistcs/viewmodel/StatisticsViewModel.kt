package com.dev_bayan_ibrahim.flashcards.ui.screen.statistcs.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev_bayan_ibrahim.flashcards.data.model.statistics.TimeGroup
import com.dev_bayan_ibrahim.flashcards.data.model.statistics.TimeStatisticsItem
import com.dev_bayan_ibrahim.flashcards.data.repo.FlashRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
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
}