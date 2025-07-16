// iosMain/kotlin/platform/Settings.ios.kt
package io.github.warforged5.mashkmp.platform

import platform.Foundation.NSUserDefaults
import platform.Foundation.NSBundle
import androidx.compose.runtime.Composable

actual class Settings {
    private val userDefaults = NSUserDefaults.standardUserDefaults

    actual fun getString(key: String, defaultValue: String?): String? {
        val value = userDefaults.stringForKey(key)
        return if (value != null) value else defaultValue
    }

    actual fun putString(key: String, value: String) {
        userDefaults.setObject(value, key)
        userDefaults.synchronize()
    }

    actual fun remove(key: String) {
        userDefaults.removeObjectForKey(key)
        userDefaults.synchronize()
    }

    actual fun clear() {
        val domain = NSBundle.mainBundle.bundleIdentifier
        if (domain != null) {
            userDefaults.removePersistentDomainForName(domain)
            userDefaults.synchronize()
        }
    }
}

@Composable
actual fun createSettings(): Settings {
    return Settings()
}