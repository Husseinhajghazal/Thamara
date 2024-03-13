package com.dev_bayan_ibrahim.flashcards.ui.screen.home.viewmodel

import androidx.lifecycle.ViewModel
import com.dev_bayan_ibrahim.flashcards.data.model.user.MutableUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

): ViewModel(), HomeUiActions {
    val state = HomeMutableUiState()

    override fun onNameChange(name: String) {
        state.newUserName = name
    }

    override fun onAgeChange(age: Int) {
        state.newUserAge = age
    }

    override fun onSave() {
        state.user = MutableUser(
            name = state.newUserName,
            age = state.newUserAge
        )
    }
}