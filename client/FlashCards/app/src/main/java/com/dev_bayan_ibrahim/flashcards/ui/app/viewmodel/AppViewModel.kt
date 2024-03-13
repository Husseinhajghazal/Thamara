package com.dev_bayan_ibrahim.flashcards.ui.app.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(): ViewModel() {
    val state = AppMutableUiState()
}