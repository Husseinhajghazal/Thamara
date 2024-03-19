package com.dev_bayan_ibrahim.flashcards.ui.screen.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev_bayan_ibrahim.flashcards.data.model.statistics.GeneralStatistics
import com.dev_bayan_ibrahim.flashcards.data.repo.FlashRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: FlashRepo,
) : ViewModel(), HomeUiActions {
    val state = HomeMutableUiState()
    val user = repo
        .getUser()
        .combine(repo.getTotalPlaysCount()) { user, totalPlays ->
            user?.copy(totalPlays = totalPlays)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val generalStatistics = repo
        .getGeneralStatistics()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = GeneralStatistics()
        )


    init {
        viewModelScope.launch {
            repo.initializedDb()
        }
    }

    override fun onNameChange(name: String) {
        state.newUserName = name
    }

    override fun onAgeChange(age: Int) {
        state.newUserAge = age
    }

    override fun onSave() {
        viewModelScope.launch {
            repo.setUser(
                name = state.newUserName,
                age = state.newUserAge
            )
        }
    }
}