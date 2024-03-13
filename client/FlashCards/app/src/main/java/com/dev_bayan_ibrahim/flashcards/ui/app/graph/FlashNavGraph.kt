package com.dev_bayan_ibrahim.flashcards.ui.app.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dev_bayan_ibrahim.flashcards.ui.app.graph.util.FlashNavActions
import com.dev_bayan_ibrahim.flashcards.ui.app.graph.util.FlashNavRoutes
import com.dev_bayan_ibrahim.flashcards.ui.screen.deck_play.PlayRoute
import com.dev_bayan_ibrahim.flashcards.ui.screen.decks.DecksRoute
import com.dev_bayan_ibrahim.flashcards.ui.screen.home.HomeRoute
import com.dev_bayan_ibrahim.flashcards.ui.screen.statistcs.StatisticsRoute


@Composable
fun FlashNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    navActions: FlashNavActions
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = FlashNavRoutes.TopLevel.Home.route
    ) {
        FlashNavRoutes.TopLevel.entries.forEach { screen ->
            composable(screen.route) {
                when (screen) {
                    FlashNavRoutes.TopLevel.Home -> {
                        HomeRoute()
                    }

                    FlashNavRoutes.TopLevel.Decks -> {
                        DecksRoute()
                    }

                    FlashNavRoutes.TopLevel.Statistics -> {
                        StatisticsRoute()
                    }
                }
            }
        }
        composable(
            route = FlashNavRoutes.Play.route
        ) {
            PlayRoute()
        }
    }
}