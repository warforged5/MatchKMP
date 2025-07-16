package io.github.warforged5.mashkmp.platform

import androidx.compose.runtime.Composable

expect class Settings {
    fun getString(key: String, defaultValue: String?): String?
    fun putString(key: String, value: String)
    fun remove(key: String)
    fun clear()
}

@Composable
expect fun createSettings(): Settings
