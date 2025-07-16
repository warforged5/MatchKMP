package io.github.warforged5.mash.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import io.github.warforged5.mashkmp.platform.Settings

enum class DarkModePreference {
    SYSTEM,
    LIGHT,
    DARK
}

class ThemeManager(private val settings: Settings) {
    var currentTheme by mutableStateOf(loadTheme())
        private set

    private var _darkModePreference by mutableStateOf(loadDarkModePreference())
    val darkModePreference: DarkModePreference get() = _darkModePreference

    fun setTheme(theme: AppTheme) {
        currentTheme = theme
        saveTheme(theme)
    }

    fun setDarkModePreference(preference: DarkModePreference) {
        _darkModePreference = preference
        saveDarkModePreference(preference)
    }

    @Composable
    fun isDarkTheme(): Boolean {
        return when (darkModePreference) {
            DarkModePreference.SYSTEM -> isSystemInDarkTheme()
            DarkModePreference.LIGHT -> false
            DarkModePreference.DARK -> true
        }
    }

    private fun loadTheme(): AppTheme {
        val themeName = settings.getString("selected_theme", AppTheme.MYSTICAL.name) ?: AppTheme.MYSTICAL.name
        return try {
            AppTheme.valueOf(themeName)
        } catch (e: IllegalArgumentException) {
            AppTheme.MYSTICAL
        }
    }

    private fun saveTheme(theme: AppTheme) {
        settings.putString("selected_theme", theme.name)
    }

    private fun loadDarkModePreference(): DarkModePreference {
        val prefName = settings.getString("dark_mode_preference", DarkModePreference.SYSTEM.name)
            ?: DarkModePreference.SYSTEM.name
        return try {
            DarkModePreference.valueOf(prefName)
        } catch (e: IllegalArgumentException) {
            DarkModePreference.SYSTEM
        }
    }

    private fun saveDarkModePreference(preference: DarkModePreference) {
        settings.putString("dark_mode_preference", preference.name)
    }
}

// Helper functions for display
fun DarkModePreference.getDisplayName(): String = when (this) {
    DarkModePreference.SYSTEM -> "Follow System"
    DarkModePreference.LIGHT -> "Light Mode"
    DarkModePreference.DARK -> "Dark Mode"
}

fun DarkModePreference.getIcon(): String = when (this) {
    DarkModePreference.SYSTEM -> "🔄"
    DarkModePreference.LIGHT -> "☀️"
    DarkModePreference.DARK -> "🌙"
}