// Theme.kt - Multiplatform compatible
package io.github.warforged5.mashkmp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import io.github.warforged5.mash.ui.theme.Typography

// Mystical Theme (Purple/Violet)
private val MysticalDarkColorScheme = darkColorScheme(
    primary = Color(0xFF9C27B0),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF4A148C),
    onPrimaryContainer = Color(0xFFE1BEE7),
    secondary = Color(0xFF673AB7),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF311B92),
    onSecondaryContainer = Color(0xFFD1C4E9),
    tertiary = Color(0xFF8E24AA),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFF4A148C),
    onTertiaryContainer = Color(0xFFE1BEE7),
    error = Color(0xFFE91E63),
    errorContainer = Color(0xFF93000A),
    onError = Color(0xFFFFFFFF),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF0D0D0D),
    onBackground = Color(0xFFE6E1E5),
    surface = Color(0xFF1A1A1A),
    onSurface = Color(0xFFE6E1E5),
    surfaceVariant = Color(0xFF2D2D2D),
    onSurfaceVariant = Color(0xFFCAC4D0),
    outline = Color(0xFF938F99),
    inverseOnSurface = Color(0xFF1C1B1F),
    inverseSurface = Color(0xFFE6E1E5),
    inversePrimary = Color(0xFF6750A4),
    surfaceTint = Color(0xFF9C27B0),
    outlineVariant = Color(0xFF49454F),
    scrim = Color(0xFF000000),
)

private val MysticalLightColorScheme = lightColorScheme(
    primary = Color(0xFF6750A4),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFEADDFF),
    onPrimaryContainer = Color(0xFF21005D),
    secondary = Color(0xFF625B71),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFE8DEF8),
    onSecondaryContainer = Color(0xFF1D192B),
    tertiary = Color(0xFF7D5260),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFD8E4),
    onTertiaryContainer = Color(0xFF31111D),
    error = Color(0xFFBA1A1A),
    errorContainer = Color(0xFFFFDAD6),
    onError = Color(0xFFFFFFFF),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFFFBFE),
    onBackground = Color(0xFF1C1B1F),
    surface = Color(0xFFFFFBFE),
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFE7E0EC),
    onSurfaceVariant = Color(0xFF49454F),
    outline = Color(0xFF79747E),
    inverseOnSurface = Color(0xFFF4EFF4),
    inverseSurface = Color(0xFF313033),
    inversePrimary = Color(0xFFD0BCFF),
    surfaceTint = Color(0xFF6750A4),
    outlineVariant = Color(0xFFCAC4D0),
    scrim = Color(0xFF000000),
)

// Ocean Theme (Blue/Teal)
private val OceanDarkColorScheme = darkColorScheme(
    primary = Color(0xFF0077BE),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF003D75),
    onPrimaryContainer = Color(0xFFB3E5FC),
    secondary = Color(0xFF00BCD4),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF006064),
    onSecondaryContainer = Color(0xFFB2EBF2),
    tertiary = Color(0xFF0097A7),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFF004D57),
    onTertiaryContainer = Color(0xFFB2DFDB),
    background = Color(0xFF0D1117),
    onBackground = Color(0xFFE1F5FE),
    surface = Color(0xFF1A1A1A),
    onSurface = Color(0xFFE1F5FE),
    surfaceVariant = Color(0xFF2D3748),
    onSurfaceVariant = Color(0xFFCAD5E0),
    outline = Color(0xFF79747E),
    inverseOnSurface = Color(0xFF1C1B1F),
    inverseSurface = Color(0xFFE1F5FE),
    inversePrimary = Color(0xFF0077BE),
    surfaceTint = Color(0xFF0077BE),
    outlineVariant = Color(0xFF49454F),
    scrim = Color(0xFF000000),
)

private val OceanLightColorScheme = lightColorScheme(
    primary = Color(0xFF0077BE),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFB3E5FC),
    onPrimaryContainer = Color(0xFF003D75),
    secondary = Color(0xFF00BCD4),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFB2EBF2),
    onSecondaryContainer = Color(0xFF006064),
    tertiary = Color(0xFF0097A7),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFB2DFDB),
    onTertiaryContainer = Color(0xFF004D57),
    background = Color(0xFFF8FDFF),
    onBackground = Color(0xFF001F25),
    surface = Color(0xFFF8FDFF),
    onSurface = Color(0xFF001F25),
    surfaceVariant = Color(0xFFE0F2F1),
    onSurfaceVariant = Color(0xFF3F4A4C),
    outline = Color(0xFF79747E),
    inverseOnSurface = Color(0xFFF4EFF4),
    inverseSurface = Color(0xFF313033),
    inversePrimary = Color(0xFFB3E5FC),
    surfaceTint = Color(0xFF0077BE),
    outlineVariant = Color(0xFFCAC4D0),
    scrim = Color(0xFF000000),
)

// Forest Theme (Green/Natural)
private val ForestDarkColorScheme = darkColorScheme(
    primary = Color(0xFF4CAF50),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF2E7D32),
    onPrimaryContainer = Color(0xFFC8E6C9),
    secondary = Color(0xFF8BC34A),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF558B2F),
    onSecondaryContainer = Color(0xFFDCEDC8),
    tertiary = Color(0xFF66BB6A),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFF388E3C),
    onTertiaryContainer = Color(0xFFC8E6C9),
    background = Color(0xFF0A1B0A),
    onBackground = Color(0xFFE8F5E8),
    surface = Color(0xFF1A1A1A),
    onSurface = Color(0xFFE8F5E8),
    surfaceVariant = Color(0xFF2D4A2D),
    onSurfaceVariant = Color(0xFFC4D5C4),
    outline = Color(0xFF79747E),
    inverseOnSurface = Color(0xFF1C1B1F),
    inverseSurface = Color(0xFFE8F5E8),
    inversePrimary = Color(0xFF4CAF50),
    surfaceTint = Color(0xFF4CAF50),
    outlineVariant = Color(0xFF49454F),
    scrim = Color(0xFF000000),
)

private val ForestLightColorScheme = lightColorScheme(
    primary = Color(0xFF4CAF50),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFC8E6C9),
    onPrimaryContainer = Color(0xFF2E7D32),
    secondary = Color(0xFF8BC34A),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFDCEDC8),
    onSecondaryContainer = Color(0xFF558B2F),
    tertiary = Color(0xFF66BB6A),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFC8E6C9),
    onTertiaryContainer = Color(0xFF388E3C),
    background = Color(0xFFF8FFF8),
    onBackground = Color(0xFF1B5E1F),
    surface = Color(0xFFF8FFF8),
    onSurface = Color(0xFF1B5E1F),
    surfaceVariant = Color(0xFFE8F5E8),
    onSurfaceVariant = Color(0xFF424242),
    outline = Color(0xFF79747E),
    inverseOnSurface = Color(0xFFF4EFF4),
    inverseSurface = Color(0xFF313033),
    inversePrimary = Color(0xFFC8E6C9),
    surfaceTint = Color(0xFF4CAF50),
    outlineVariant = Color(0xFFCAC4D0),
    scrim = Color(0xFF000000),
)

// Sunset Theme (Orange/Pink)
private val SunsetDarkColorScheme = darkColorScheme(
    primary = Color(0xFFFF5722),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFBF360C),
    onPrimaryContainer = Color(0xFFFFCCBC),
    secondary = Color(0xFFE91E63),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFAD1457),
    onSecondaryContainer = Color(0xFFF8BBD9),
    tertiary = Color(0xFFFF9800),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFE65100),
    onTertiaryContainer = Color(0xFFFFE0B2),
    background = Color(0xFF1A0D0D),
    onBackground = Color(0xFFFFF3E0),
    surface = Color(0xFF1A1A1A),
    onSurface = Color(0xFFFFF3E0),
    surfaceVariant = Color(0xFF4A2D2D),
    onSurfaceVariant = Color(0xFFD5C4C4),
    outline = Color(0xFF79747E),
    inverseOnSurface = Color(0xFF1C1B1F),
    inverseSurface = Color(0xFFFFF3E0),
    inversePrimary = Color(0xFFFF5722),
    surfaceTint = Color(0xFFFF5722),
    outlineVariant = Color(0xFF49454F),
    scrim = Color(0xFF000000),
)

private val SunsetLightColorScheme = lightColorScheme(
    primary = Color(0xFFFF5722),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFFCCBC),
    onPrimaryContainer = Color(0xFFBF360C),
    secondary = Color(0xFFE91E63),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFF8BBD9),
    onSecondaryContainer = Color(0xFFAD1457),
    tertiary = Color(0xFFFF9800),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFE0B2),
    onTertiaryContainer = Color(0xFFE65100),
    background = Color(0xFFFFFBF8),
    onBackground = Color(0xFF3E2723),
    surface = Color(0xFFFFFBF8),
    onSurface = Color(0xFF3E2723),
    surfaceVariant = Color(0xFFFFF3E0),
    onSurfaceVariant = Color(0xFF5D4037),
    outline = Color(0xFF79747E),
    inverseOnSurface = Color(0xFFF4EFF4),
    inverseSurface = Color(0xFF313033),
    inversePrimary = Color(0xFFFFCCBC),
    surfaceTint = Color(0xFFFF5722),
    outlineVariant = Color(0xFFCAC4D0),
    scrim = Color(0xFF000000),
)

// Lavender Theme (Soft Purple/Pink)
private val LavenderDarkColorScheme = darkColorScheme(
    primary = Color(0xFFBA68C8),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF7B1FA2),
    onPrimaryContainer = Color(0xFFE1BEE7),
    secondary = Color(0xFFCE93D8),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF8E24AA),
    onSecondaryContainer = Color(0xFFF3E5F5),
    tertiary = Color(0xFFF48FB1),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFC2185B),
    onTertiaryContainer = Color(0xFFFCE4EC),
    background = Color(0xFF1A0D1A),
    onBackground = Color(0xFFF3E5F5),
    surface = Color(0xFF1A1A1A),
    onSurface = Color(0xFFF3E5F5),
    surfaceVariant = Color(0xFF4A2D4A),
    onSurfaceVariant = Color(0xFFD5C4D5),
    outline = Color(0xFF79747E),
    inverseOnSurface = Color(0xFF1C1B1F),
    inverseSurface = Color(0xFFF3E5F5),
    inversePrimary = Color(0xFFBA68C8),
    surfaceTint = Color(0xFFBA68C8),
    outlineVariant = Color(0xFF49454F),
    scrim = Color(0xFF000000),
)

private val LavenderLightColorScheme = lightColorScheme(
    primary = Color(0xFFBA68C8),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFE1BEE7),
    onPrimaryContainer = Color(0xFF7B1FA2),
    secondary = Color(0xFFCE93D8),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFF3E5F5),
    onSecondaryContainer = Color(0xFF8E24AA),
    tertiary = Color(0xFFF48FB1),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFCE4EC),
    onTertiaryContainer = Color(0xFFC2185B),
    background = Color(0xFFFCF8FC),
    onBackground = Color(0xFF4A148C),
    surface = Color(0xFFFCF8FC),
    onSurface = Color(0xFF4A148C),
    surfaceVariant = Color(0xFFF3E5F5),
    onSurfaceVariant = Color(0xFF6A1B9A),
    outline = Color(0xFF79747E),
    inverseOnSurface = Color(0xFFF4EFF4),
    inverseSurface = Color(0xFF313033),
    inversePrimary = Color(0xFFE1BEE7),
    surfaceTint = Color(0xFFBA68C8),
    outlineVariant = Color(0xFFCAC4D0),
    scrim = Color(0xFF000000),
)

enum class AppTheme {
    SYSTEM,
    MYSTICAL,
    OCEAN,
    FOREST,
    SUNSET,
    LAVENDER
}

@Composable
fun MashTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    selectedTheme: AppTheme = AppTheme.MYSTICAL,
    content: @Composable () -> Unit
) {
    val colorScheme = when (selectedTheme) {
        AppTheme.SYSTEM -> {
            // Use Mystical as default since we can't access dynamic colors in multiplatform common code
            if (darkTheme) MysticalDarkColorScheme else MysticalLightColorScheme
        }
        AppTheme.MYSTICAL -> {
            if (darkTheme) MysticalDarkColorScheme else MysticalLightColorScheme
        }
        AppTheme.OCEAN -> {
            if (darkTheme) OceanDarkColorScheme else OceanLightColorScheme
        }
        AppTheme.FOREST -> {
            if (darkTheme) ForestDarkColorScheme else ForestLightColorScheme
        }
        AppTheme.SUNSET -> {
            if (darkTheme) SunsetDarkColorScheme else SunsetLightColorScheme
        }
        AppTheme.LAVENDER -> {
            if (darkTheme) LavenderDarkColorScheme else LavenderLightColorScheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Theme helper functions
fun AppTheme.getDisplayName(): String = when (this) {
    AppTheme.SYSTEM -> "System Default"
    AppTheme.MYSTICAL -> "Mystical Purple"
    AppTheme.OCEAN -> "Ocean Blue"
    AppTheme.FOREST -> "Forest Green"
    AppTheme.SUNSET -> "Sunset Orange"
    AppTheme.LAVENDER -> "Lavender Dreams"
}

fun AppTheme.getIcon(): String = when (this) {
    AppTheme.SYSTEM -> "🎨"
    AppTheme.MYSTICAL -> "🔮"
    AppTheme.OCEAN -> "🌊"
    AppTheme.FOREST -> "🌲"
    AppTheme.SUNSET -> "🌅"
    AppTheme.LAVENDER -> "💜"
}