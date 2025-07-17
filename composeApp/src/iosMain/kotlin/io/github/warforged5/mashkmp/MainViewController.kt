import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import io.github.warforged5.mash.MashApp
import io.github.warforged5.mashkmp.ui.theme.MashTheme
import io.github.warforged5.mash.ui.theme.ThemeManager
import io.github.warforged5.mashkmp.platform.createSettings

fun MainViewController() = ComposeUIViewController {
    // Create settings for ThemeManager using the platform-specific implementation
    val settings = createSettings()
    val themeManager = remember { ThemeManager(settings) }

    val currentTheme by remember { derivedStateOf { themeManager.currentTheme } }
    val isDarkTheme = themeManager.isDarkTheme()

    MashTheme(
        darkTheme = isDarkTheme,
        selectedTheme = currentTheme,
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