package com.dev_bayan_ibrahim.flashcards.ui.screen.home.viewmodel

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.dev_bayan_ibrahim.flashcards.data.model.user.MutableUser
import com.dev_bayan_ibrahim.flashcards.data.model.user.User

@Stable
interface HomeUiState {
    val user: User?
    val newUserName: String
    val newUserAge: Int
}

class HomeMutableUiState : HomeUiState {
    override var user: MutableUser? by mutableStateOf(null)
    override var newUserName: String by mutableStateOf("")
    override var newUserAge: Int by mutableIntStateOf(10)
}