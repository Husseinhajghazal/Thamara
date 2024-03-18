package com.dev_bayan_ibrahim.flashcards.ui.screen.home


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.flashcards.data.model.statistics.GeneralStatistics
import com.dev_bayan_ibrahim.flashcards.data.model.user.User
import com.dev_bayan_ibrahim.flashcards.ui.screen.home.component.HomeGeneralStatistics
import com.dev_bayan_ibrahim.flashcards.ui.screen.home.component.HomeUser
import com.dev_bayan_ibrahim.flashcards.ui.screen.home.component.HomeUserDialog
import com.dev_bayan_ibrahim.flashcards.ui.screen.home.viewmodel.HomeMutableUiState
import com.dev_bayan_ibrahim.flashcards.ui.screen.home.viewmodel.HomeUiActions
import com.dev_bayan_ibrahim.flashcards.ui.screen.home.viewmodel.HomeUiState
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeUiState,
    user: User?,
    generalStatistics: GeneralStatistics,
    actions: HomeUiActions,
) {
    HomeUserDialog(
        show = user == null,
        name = state.newUserName,
        age = state.newUserAge,
        onNameChange = actions::onNameChange,
        onAgeChange = actions::onAgeChange,
        onSave = actions::onSave
    )
    Column(
        modifier = modifier,
    ) {
        HomeUser(
            modifier = Modifier
                .padding(horizontal = 8.dp),
            user = user,
        )
        HomeGeneralStatistics(statistics = generalStatistics)
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreviewLight() {
    FlashCardsTheme(darkTheme = false) {
        Surface(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.background,
        ) {
            val state = HomeMutableUiState()
            val actions = object : HomeUiActions {
                override fun onNameChange(name: String) {
                }

                override fun onAgeChange(age: Int) {
                }

                override fun onSave() {
                }
            }
            HomeScreen(
                state = state,
                actions = actions,
                generalStatistics = GeneralStatistics(),
                user = User(),
            )
        }
    }
}
