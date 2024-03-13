package com.dev_bayan_ibrahim.flashcards.ui.screen.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.dev_bayan_ibrahim.flashcards.ui.screen.home.viewmodel.HomeViewModel


@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    HomeScreen(
        modifier = modifier,
        state = homeViewModel.state,
        actions = homeViewModel
    )
}