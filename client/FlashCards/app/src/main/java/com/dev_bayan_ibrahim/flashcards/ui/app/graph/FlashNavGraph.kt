package com.dev_bayan_ibrahim.flashcards.ui.app.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavArgs
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dev_bayan_ibrahim.flashcards.ui.app.graph.util.FlashNavActions
import com.dev_bayan_ibrahim.flashcards.ui.app.graph.util.FlashNavRoutes
import com.dev_bayan_ibrahim.flashcards.ui.app.graph.util.FlashNavRoutes.Play
import com.dev_bayan_ibrahim.flashcards.ui.app.graph.util.FlashNavRoutes.TopLevel
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
        startDestination = TopLevel.Home.route
    ) {
        TopLevel.entries.forEach { screen ->
            composable(screen.route) {
                when (screen) {
                    TopLevel.Home -> {
                        HomeRoute()
                    }

                    TopLevel.Decks -> {
                        DecksRoute {
                            navActions.navigateTo(Play.getDestination(it))
                        }
                    }

                    TopLevel.Statistics -> {
                        StatisticsRoute()
                    }
                }
            }
        }
        composable(
            route = Play.route,
            arguments = listOf(
                navArgument(Play.Arg.id.name) {
                    type = NavType.LongType
                }
            )
        ) {
            it.arguments?.getLong(Play.Arg.id.name)?.let { deckId ->
                PlayRoute(
                    id = deckId,
                    navigateUp = navActions::navigateUp
                )
            } ?: navActions.navigateUp()
        }
    }
}