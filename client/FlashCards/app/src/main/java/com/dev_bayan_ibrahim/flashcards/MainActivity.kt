package com.dev_bayan_ibrahim.flashcards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.dev_bayan_ibrahim.flashcards.ui.app.FlashApp
import com.dev_bayan_ibrahim.flashcards.ui.theme.FlashCardsTheme
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlashCardsTheme {
                val windowSize = calculateWindowSizeClass(this)
                FlashApp(widthSizeClass = windowSize.widthSizeClass)
            }
        }
    }
}
