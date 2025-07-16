package io.github.warforged5.mashkmp

import io.github.warforged5.mash.MashApp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import io.github.warforged5.mash.ui.theme.MashTheme
import io.github.warforged5.mash.ui.theme.ThemeManager
import io.github.warforged5.mashkmp.platform.Settings

class MainActivity : ComponentActivity() {
    private lateinit var themeManager: ThemeManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create settings for ThemeManager
        val preferences = getSharedPreferences("theme_prefs", MODE_PRIVATE)
        val settings = Settings(preferences)
        themeManager = ThemeManager(settings)

        setContent {
            val currentTheme by remember { derivedStateOf { themeManager.currentTheme } }
            val isDarkTheme = themeManager.isDarkTheme()

            MashTheme(
                darkTheme = isDarkTheme,
                selectedTheme = currentTheme,
                dynamicColor = false // Disable dynamic color to show custom themes
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    MashApp(themeManager = themeManager)
                }
            }
        }
    }
}