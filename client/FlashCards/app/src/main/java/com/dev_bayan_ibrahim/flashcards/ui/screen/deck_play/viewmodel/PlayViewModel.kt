package com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlayViewModel @Inject constructor(

): ViewModel(), PlayUiActions {
    val state = PlayMutableUiState()

    override fun onSelectAnswer(value: String) {

    }
}