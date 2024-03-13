package com.dev_bayan_ibrahim.flashcards.ui.app


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dev_bayan_ibrahim.flashcards.ui.app.bar.FlashBottomBar
import com.dev_bayan_ibrahim.flashcards.ui.app.graph.FlashNavGraph
import com.dev_bayan_ibrahim.flashcards.ui.app.graph.util.FlashNavActions
import com.dev_bayan_ibrahim.flashcards.ui.app.graph.util.FlashNavRoutes
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme

@Composable
fun FlashApp(
    modifier: Modifier = Modifier,
    widthSizeClass: WindowWidthSizeClass,
) {
    val navController = rememberNavController()
    val navActions by remember (navController) {
        mutableStateOf(FlashNavActions(navController))
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute by remember(navBackStackEntry) {
        derivedStateOf {
            navBackStackEntry?.destination?.route?.run {
                FlashNavRoutes.byRoute(this)
            } ?: FlashNavRoutes.TopLevel.Home
        }
    }

    Scaffold (
        bottomBar = {
            FlashBottomBar(
                selected = currentRoute,
                onClick = {
                    navActions.navigateToTopLevel(it)
                }
            )
        }
    ) {
        FlashNavGraph(
            modifier = Modifier
                .padding(it),
            navController = navController,
            navActions = navActions,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FlashAppPreviewLight() {
    FlashCardsTheme(darkTheme = false) {
        Surface(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.background,
        ) {
            FlashApp(
                widthSizeClass = WindowWidthSizeClass.Compact
            )
        }
    }
}
