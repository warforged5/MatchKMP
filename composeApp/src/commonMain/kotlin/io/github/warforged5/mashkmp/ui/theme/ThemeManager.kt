package io.github.warforged5.mash.ui.theme

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

enum class DarkModePreference {
    SYSTEM,
    LIGHT,
    DARK
}

class ThemeManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)

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
        val themeName = prefs.getString("selected_theme", AppTheme.MYSTICAL.name) ?: AppTheme.MYSTICAL.name
        return try {
            AppTheme.valueOf(themeName)
        } catch (e: IllegalArgumentException) {
            AppTheme.MYSTICAL
        }
    }

    private fun saveTheme(theme: AppTheme) {
        prefs.edit().putString("selected_theme", theme.name).apply()
    }

    private fun loadDarkModePreference(): DarkModePreference {
        val prefName = prefs.getString("dark_mode_preference", DarkModePreference.SYSTEM.name)
            ?: DarkModePreference.SYSTEM.name
        return try {
            DarkModePreference.valueOf(prefName)
        } catch (e: IllegalArgumentException) {
            DarkModePreference.SYSTEM
        }
    }

    private fun saveDarkModePreference(preference: DarkModePreference) {
        prefs.edit().putString("dark_mode_preference", preference.name).apply()
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