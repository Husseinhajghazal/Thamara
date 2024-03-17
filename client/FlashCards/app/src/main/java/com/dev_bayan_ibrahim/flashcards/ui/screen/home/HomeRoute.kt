package com.dev_bayan_ibrahim.flashcards.ui.screen.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.dev_bayan_ibrahim.flashcards.ui.screen.home.viewmodel.HomeViewModel


@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val user by homeViewModel.user.collectAsState()
    val generalStatistics by homeViewModel.generalStatistics.collectAsState()
    val timedStatistics by homeViewModel.timedStatistics.collectAsState()
    HomeScreen(
        modifier = modifier,
        state = homeViewModel.state,
        user = user,
        generalStatistics = generalStatistics,
        timedStatistics = timedStatistics,
        actions = homeViewModel
    )
}