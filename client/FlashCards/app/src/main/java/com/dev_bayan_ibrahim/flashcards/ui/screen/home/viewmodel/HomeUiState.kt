package com.dev_bayan_ibrahim.flashcards.ui.screen.home.viewmodel

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Stable
interface HomeUiState {
    val newUserName: String
    val newUserAge: Int
}

class HomeMutableUiState : HomeUiState {
    override var newUserName: String by mutableStateOf("")
    override var newUserAge: Int by mutableIntStateOf(10)
}