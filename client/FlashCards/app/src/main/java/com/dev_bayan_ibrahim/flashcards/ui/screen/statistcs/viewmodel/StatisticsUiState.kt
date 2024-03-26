package com.dev_bayan_ibrahim.flashcards.ui.screen.statistcs.viewmodel

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.dev_bayan_ibrahim.flashcards.data.model.user.UserRank

@Stable
interface StatisticsUiState {
    val rankStatistics: List<UserRank>
}

class StatisticsMutableUiState : StatisticsUiState {
    override val rankStatistics: SnapshotStateList<UserRank> = mutableStateListOf()
}