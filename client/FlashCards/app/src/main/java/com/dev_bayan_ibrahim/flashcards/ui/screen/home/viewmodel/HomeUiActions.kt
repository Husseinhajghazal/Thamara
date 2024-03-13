package com.dev_bayan_ibrahim.flashcards.ui.screen.home.viewmodel

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Immutable
interface HomeUiActions {
    fun onNameChange(name: String)
    fun onAgeChange(age: Int)
    fun onSave()
}