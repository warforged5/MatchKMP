package io.github.warforged5.mashkmp.platform

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.Composable

actual class Settings(private val preferences: SharedPreferences) {
    actual fun getString(key: String, defaultValue: String?): String? {
        return preferences.getString(key, defaultValue)
    }

    actual fun putString(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }

    actual fun remove(key: String) {
        preferences.edit().remove(key).apply()
    }

    actual fun clear() {
        preferences.edit().clear().apply()
    }
}

@Composable
actual fun createSettings(): Settings {
    val context = LocalContext.current
    val preferences = context.getSharedPreferences("mash_prefs", Context.MODE_PRIVATE)
    return Settings(preferences)
}