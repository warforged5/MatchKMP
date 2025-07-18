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

// NEW THEMES BELOW

// Neon Theme (Cyberpunk/Bright)
private val NeonDarkColorScheme = darkColorScheme(
    primary = Color(0xFFFF0080),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF990050),
    onPrimaryContainer = Color(0xFFFFB3D9),
    secondary = Color(0xFF00FF80),
    onSecondary = Color(0xFF000000),
    secondaryContainer = Color(0xFF009950),
    onSecondaryContainer = Color(0xFFB3FFD9),
    tertiary = Color(0xFF0080FF),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFF005099),
    onTertiaryContainer = Color(0xFFB3D9FF),
    background = Color(0xFF0A0A0A),
    onBackground = Color(0xFFE0E0E0),
    surface = Color(0xFF1A1A1A),
    onSurface = Color(0xFFE0E0E0),
    surfaceVariant = Color(0xFF2A2A2A),
    onSurfaceVariant = Color(0xFFD0D0D0),
    outline = Color(0xFF808080),
    inverseOnSurface = Color(0xFF1A1A1A),
    inverseSurface = Color(0xFFE0E0E0),
    inversePrimary = Color(0xFFFF0080),
    surfaceTint = Color(0xFFFF0080),
    outlineVariant = Color(0xFF404040),
    scrim = Color(0xFF000000),
)

private val NeonLightColorScheme = lightColorScheme(
    primary = Color(0xFFE6007A),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFFB3E0),
    onPrimaryContainer = Color(0xFF99004D),
    secondary = Color(0xFF00E673),
    onSecondary = Color(0xFF000000),
    secondaryContainer = Color(0xFFB3FFD9),
    onSecondaryContainer = Color(0xFF00994D),
    tertiary = Color(0xFF0073E6),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFB3D9FF),
    onTertiaryContainer = Color(0xFF004D99),
    background = Color(0xFFFFFFF8),
    onBackground = Color(0xFF1A1A1A),
    surface = Color(0xFFFFFFF8),
    onSurface = Color(0xFF1A1A1A),
    surfaceVariant = Color(0xFFF0F0F0),
    onSurfaceVariant = Color(0xFF404040),
    outline = Color(0xFF808080),
    inverseOnSurface = Color(0xFFF0F0F0),
    inverseSurface = Color(0xFF303030),
    inversePrimary = Color(0xFFFFB3E0),
    surfaceTint = Color(0xFFE6007A),
    outlineVariant = Color(0xFFC0C0C0),
    scrim = Color(0xFF000000),
)

// Cosmic Theme (Deep Space)
private val CosmicDarkColorScheme = darkColorScheme(
    primary = Color(0xFF8A2BE2),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF4B0082),
    onPrimaryContainer = Color(0xFFE6D9FF),
    secondary = Color(0xFF483D8B),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF2F1B69),
    onSecondaryContainer = Color(0xFFD9D0FF),
    tertiary = Color(0xFF9932CC),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFF66199A),
    onTertiaryContainer = Color(0xFFE6B3FF),
    background = Color(0xFF0B0B1F),
    onBackground = Color(0xFFE6E6FF),
    surface = Color(0xFF1A1A2E),
    onSurface = Color(0xFFE6E6FF),
    surfaceVariant = Color(0xFF2D2D4A),
    onSurfaceVariant = Color(0xFFD0D0E6),
    outline = Color(0xFF8080A0),
    inverseOnSurface = Color(0xFF1A1A2E),
    inverseSurface = Color(0xFFE6E6FF),
    inversePrimary = Color(0xFF8A2BE2),
    surfaceTint = Color(0xFF8A2BE2),
    outlineVariant = Color(0xFF40405A),
    scrim = Color(0xFF000000),
)

private val CosmicLightColorScheme = lightColorScheme(
    primary = Color(0xFF7B68EE),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFE6D9FF),
    onPrimaryContainer = Color(0xFF4B0082),
    secondary = Color(0xFF6A5ACD),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFD9D0FF),
    onSecondaryContainer = Color(0xFF2F1B69),
    tertiary = Color(0xFFDDA0DD),
    onTertiary = Color(0xFF663366),
    tertiaryContainer = Color(0xFFE6B3FF),
    onTertiaryContainer = Color(0xFF66199A),
    background = Color(0xFFFCFCFF),
    onBackground = Color(0xFF1A1A2E),
    surface = Color(0xFFFCFCFF),
    onSurface = Color(0xFF1A1A2E),
    surfaceVariant = Color(0xFFF0F0FF),
    onSurfaceVariant = Color(0xFF404060),
    outline = Color(0xFF808080),
    inverseOnSurface = Color(0xFFF0F0FF),
    inverseSurface = Color(0xFF303030),
    inversePrimary = Color(0xFFE6D9FF),
    surfaceTint = Color(0xFF7B68EE),
    outlineVariant = Color(0xFFC0C0D0),
    scrim = Color(0xFF000000),
)

// Cherry Blossom Theme (Soft Pink/White)
private val CherryBlossomDarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFB6C1),
    onPrimary = Color(0xFF4D1A1F),
    primaryContainer = Color(0xFF662633),
    onPrimaryContainer = Color(0xFFFFD9E0),
    secondary = Color(0xFFFFC0CB),
    onSecondary = Color(0xFF4D1D22),
    secondaryContainer = Color(0xFF662936),
    onSecondaryContainer = Color(0xFFFFDBE3),
    tertiary = Color(0xFFEEB4B4),
    onTertiary = Color(0xFF4A1C1C),
    tertiaryContainer = Color(0xFF63282F),
    onTertiaryContainer = Color(0xFFFFD6D9),
    background = Color(0xFF1F1A1B),
    onBackground = Color(0xFFF0E6E8),
    surface = Color(0xFF1F1A1B),
    onSurface = Color(0xFFF0E6E8),
    surfaceVariant = Color(0xFF4A3F41),
    onSurfaceVariant = Color(0xFFD5C3C6),
    outline = Color(0xFF9F8C8F),
    inverseOnSurface = Color(0xFF1F1A1B),
    inverseSurface = Color(0xFFF0E6E8),
    inversePrimary = Color(0xFF904A56),
    surfaceTint = Color(0xFFFFB6C1),
    outlineVariant = Color(0xFF4A3F41),
    scrim = Color(0xFF000000),
)

private val CherryBlossomLightColorScheme = lightColorScheme(
    primary = Color(0xFF904A56),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFFD9E0),
    onPrimaryContainer = Color(0xFF3A0815),
    secondary = Color(0xFF925662),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFFFDBE3),
    onSecondaryContainer = Color(0xFF3B0A1E),
    tertiary = Color(0xFF8E5B5B),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFD6D9),
    onTertiaryContainer = Color(0xFF3A1819),
    background = Color(0xFFFFF8F8),
    onBackground = Color(0xFF22191A),
    surface = Color(0xFFFFF8F8),
    onSurface = Color(0xFF22191A),
    surfaceVariant = Color(0xFFF5E0E2),
    onSurfaceVariant = Color(0xFF534145),
    outline = Color(0xFF847175),
    inverseOnSurface = Color(0xFFFEEDEF),
    inverseSurface = Color(0xFF372E30),
    inversePrimary = Color(0xFFFFB6C1),
    surfaceTint = Color(0xFF904A56),
    outlineVariant = Color(0xFFD8C4C7),
    scrim = Color(0xFF000000),
)

// Fire Theme (Intense Reds/Oranges)
private val FireDarkColorScheme = darkColorScheme(
    primary = Color(0xFFFF4500),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFB22D00),
    onPrimaryContainer = Color(0xFFFFDDCC),
    secondary = Color(0xFFDC143C),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF9A0E2B),
    onSecondaryContainer = Color(0xFFFFD6DD),
    tertiary = Color(0xFFFF6347),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFB2442F),
    onTertiaryContainer = Color(0xFFFFE0DB),
    background = Color(0xFF1F0A00),
    onBackground = Color(0xFFFFF3E6),
    surface = Color(0xFF1F0A00),
    onSurface = Color(0xFFFFF3E6),
    surfaceVariant = Color(0xFF4A2B1A),
    onSurfaceVariant = Color(0xFFD5C4B8),
    outline = Color(0xFF9F8D81),
    inverseOnSurface = Color(0xFF1F0A00),
    inverseSurface = Color(0xFFFFF3E6),
    inversePrimary = Color(0xFFFF4500),
    surfaceTint = Color(0xFFFF4500),
    outlineVariant = Color(0xFF4A2B1A),
    scrim = Color(0xFF000000),
)

private val FireLightColorScheme = lightColorScheme(
    primary = Color(0xFFD63400),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFFDDCC),
    onPrimaryContainer = Color(0xFF440E00),
    secondary = Color(0xFFB91A3A),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFFFD6DD),
    onSecondaryContainer = Color(0xFF3E0010),
    tertiary = Color(0xFFCC5540),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFE0DB),
    onTertiaryContainer = Color(0xFF44100A),
    background = Color(0xFFFFFBF8),
    onBackground = Color(0xFF221A15),
    surface = Color(0xFFFFFBF8),
    onSurface = Color(0xFF221A15),
    surfaceVariant = Color(0xFFF5E6DD),
    onSurfaceVariant = Color(0xFF534740),
    outline = Color(0xFF847670),
    inverseOnSurface = Color(0xFFFEF1E9),
    inverseSurface = Color(0xFF372F2A),
    inversePrimary = Color(0xFFFFB59A),
    surfaceTint = Color(0xFFD63400),
    outlineVariant = Color(0xFFD8CAC1),
    scrim = Color(0xFF000000),
)

// Ice Theme (Cool Blues/Whites)
private val IceDarkColorScheme = darkColorScheme(
    primary = Color(0xFF87CEEB),
    onPrimary = Color(0xFF001F2E),
    primaryContainer = Color(0xFF003547),
    onPrimaryContainer = Color(0xFFB3E5FC),
    secondary = Color(0xFFB0E0E6),
    onSecondary = Color(0xFF001D21),
    secondaryContainer = Color(0xFF003033),
    onSecondaryContainer = Color(0xFFCCF7FB),
    tertiary = Color(0xFFE0F6FF),
    onTertiary = Color(0xFF001E26),
    tertiaryContainer = Color(0xFF00333D),
    onTertiaryContainer = Color(0xFFDDEEFF),
    background = Color(0xFF0E1419),
    onBackground = Color(0xFFE3F2F8),
    surface = Color(0xFF0E1419),
    onSurface = Color(0xFFE3F2F8),
    surfaceVariant = Color(0xFF3F484D),
    onSurfaceVariant = Color(0xFFBFC8CD),
    outline = Color(0xFF899297),
    inverseOnSurface = Color(0xFF0E1419),
    inverseSurface = Color(0xFFE3F2F8),
    inversePrimary = Color(0xFF00678A),
    surfaceTint = Color(0xFF87CEEB),
    outlineVariant = Color(0xFF3F484D),
    scrim = Color(0xFF000000),
)

private val IceLightColorScheme = lightColorScheme(
    primary = Color(0xFF00678A),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFB3E5FC),
    onPrimaryContainer = Color(0xFF001E2C),
    secondary = Color(0xFF4F6B70),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFCCF7FB),
    onSecondaryContainer = Color(0xFF0B1D21),
    tertiary = Color(0xFF526B7A),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFDDEEFF),
    onTertiaryContainer = Color(0xFF0E1E26),
    background = Color(0xFFF5FAFE),
    onBackground = Color(0xFF171C20),
    surface = Color(0xFFF5FAFE),
    onSurface = Color(0xFF171C20),
    surfaceVariant = Color(0xFFDDE3EA),
    onSurfaceVariant = Color(0xFF41484D),
    outline = Color(0xFF71787E),
    inverseOnSurface = Color(0xFFEDF1F6),
    inverseSurface = Color(0xFF2C3135),
    inversePrimary = Color(0xFF87CEEB),
    surfaceTint = Color(0xFF00678A),
    outlineVariant = Color(0xFFC1C7CE),
    scrim = Color(0xFF000000),
)

// Earth Theme (Browns/Tans)
private val EarthDarkColorScheme = darkColorScheme(
    primary = Color(0xFFD2B48C),
    onPrimary = Color(0xFF2E1F0A),
    primaryContainer = Color(0xFF463217),
    onPrimaryContainer = Color(0xFFEDD1A7),
    secondary = Color(0xFFCD853F),
    onSecondary = Color(0xFF2B1F0E),
    secondaryContainer = Color(0xFF423120),
    onSecondaryContainer = Color(0xFFE6C18A),
    tertiary = Color(0xFFBC9A6A),
    onTertiary = Color(0xFF281E10),
    tertiaryContainer = Color(0xFF3F3124),
    onTertiaryContainer = Color(0xFFD9B785),
    background = Color(0xFF1A1611),
    onBackground = Color(0xFFEEE8DE),
    surface = Color(0xFF1A1611),
    onSurface = Color(0xFFEEE8DE),
    surfaceVariant = Color(0xFF4C453B),
    onSurfaceVariant = Color(0xFFCFC5B8),
    outline = Color(0xFF999088),
    inverseOnSurface = Color(0xFF1A1611),
    inverseSurface = Color(0xFFEEE8DE),
    inversePrimary = Color(0xFF6B5633),
    surfaceTint = Color(0xFFD2B48C),
    outlineVariant = Color(0xFF4C453B),
    scrim = Color(0xFF000000),
)

private val EarthLightColorScheme = lightColorScheme(
    primary = Color(0xFF6B5633),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFEDD1A7),
    onPrimaryContainer = Color(0xFF241A05),
    secondary = Color(0xFF6F5B3E),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFE6C18A),
    onSecondaryContainer = Color(0xFF251A05),
    tertiary = Color(0xFF665938),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFD9B785),
    onTertiaryContainer = Color(0xFF221804),
    background = Color(0xFFFCF8F2),
    onBackground = Color(0xFF1E1B16),
    surface = Color(0xFFFCF8F2),
    onSurface = Color(0xFF1E1B16),
    surfaceVariant = Color(0xFFEDE1D1),
    onSurfaceVariant = Color(0xFF4E453B),
    outline = Color(0xFF80756A),
    inverseOnSurface = Color(0xFFF7F0E7),
    inverseSurface = Color(0xFF33302A),
    inversePrimary = Color(0xFFD2B48C),
    surfaceTint = Color(0xFF6B5633),
    outlineVariant = Color(0xFFD0C5B5),
    scrim = Color(0xFF000000),
)

// Royal Theme (Deep Blue/Gold)
private val RoyalDarkColorScheme = darkColorScheme(
    primary = Color(0xFF4169E1),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF1E3A8A),
    onPrimaryContainer = Color(0xFFBFD5FF),
    secondary = Color(0xFFFFD700),
    onSecondary = Color(0xFF2D2400),
    secondaryContainer = Color(0xFF423600),
    onSecondaryContainer = Color(0xFFFFE94D),
    tertiary = Color(0xFF800080),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFF4A004A),
    onTertiaryContainer = Color(0xFFE6B3E6),
    background = Color(0xFF0F1829),
    onBackground = Color(0xFFE6ECFF),
    surface = Color(0xFF0F1829),
    onSurface = Color(0xFFE6ECFF),
    surfaceVariant = Color(0xFF3D456B),
    onSurfaceVariant = Color(0xFFC3CBE8),
    outline = Color(0xFF8D95B1),
    inverseOnSurface = Color(0xFF0F1829),
    inverseSurface = Color(0xFFE6ECFF),
    inversePrimary = Color(0xFF4169E1),
    surfaceTint = Color(0xFF4169E1),
    outlineVariant = Color(0xFF3D456B),
    scrim = Color(0xFF000000),
)

private val RoyalLightColorScheme = lightColorScheme(
    primary = Color(0xFF1E3A8A),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFBFD5FF),
    onPrimaryContainer = Color(0xFF001946),
    secondary = Color(0xFF735F00),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFFFE94D),
    onSecondaryContainer = Color(0xFF241B00),
    tertiary = Color(0xFF7B2D7B),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFE6B3E6),
    onTertiaryContainer = Color(0xFF2E0A2E),
    background = Color(0xFFF8FAFF),
    onBackground = Color(0xFF1A1D29),
    surface = Color(0xFFF8FAFF),
    onSurface = Color(0xFF1A1D29),
    surfaceVariant = Color(0xFFE0E7F7),
    onSurfaceVariant = Color(0xFF43476E),
    outline = Color(0xFF73779F),
    inverseOnSurface = Color(0xFFF0F2FF),
    inverseSurface = Color(0xFF2F323E),
    inversePrimary = Color(0xFF8FAEFF),
    surfaceTint = Color(0xFF1E3A8A),
    outlineVariant = Color(0xFFC3CBE8),
    scrim = Color(0xFF000000),
)

// Mint Theme (Fresh Green/White)
private val MintDarkColorScheme = darkColorScheme(
    primary = Color(0xFF98FB98),
    onPrimary = Color(0xFF002114),
    primaryContainer = Color(0xFF00341F),
    onPrimaryContainer = Color(0xFFB3FFD1),
    secondary = Color(0xFF7FFFD4),
    onSecondary = Color(0xFF002019),
    secondaryContainer = Color(0xFF003328),
    onSecondaryContainer = Color(0xFFA8F5E0),
    tertiary = Color(0xFFAFEEEE),
    onTertiary = Color(0xFF001F1F),
    tertiaryContainer = Color(0xFF00322F),
    onTertiaryContainer = Color(0xFFCCFFFF),
    background = Color(0xFF0F1F17),
    onBackground = Color(0xFFE0F5E8),
    surface = Color(0xFF0F1F17),
    onSurface = Color(0xFFE0F5E8),
    surfaceVariant = Color(0xFF3E4D43),
    onSurfaceVariant = Color(0xFFBECDC3),
    outline = Color(0xFF88978D),
    inverseOnSurface = Color(0xFF0F1F17),
    inverseSurface = Color(0xFFE0F5E8),
    inversePrimary = Color(0xFF006837),
    surfaceTint = Color(0xFF98FB98),
    outlineVariant = Color(0xFF3E4D43),
    scrim = Color(0xFF000000),
)

private val MintLightColorScheme = lightColorScheme(
    primary = Color(0xFF006837),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFB3FFD1),
    onPrimaryContainer = Color(0xFF00210F),
    secondary = Color(0xFF4F6B5C),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFA8F5E0),
    onSecondaryContainer = Color(0xFF0B1D19),
    tertiary = Color(0xFF3D6B69),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFCCFFFF),
    onTertiaryContainer = Color(0xFF001F1F),
    background = Color(0xFFF0FFF4),
    onBackground = Color(0xFF161D19),
    surface = Color(0xFFF0FFF4),
    onSurface = Color(0xFF161D19),
    surfaceVariant = Color(0xFFDCE8DD),
    onSurfaceVariant = Color(0xFF404D43),
    outline = Color(0xFF707D73),
    inverseOnSurface = Color(0xFFECF3ED),
    inverseSurface = Color(0xFF2B322E),
    inversePrimary = Color(0xFF98FB98),
    surfaceTint = Color(0xFF006837),
    outlineVariant = Color(0xFFC0CCC1),
    scrim = Color(0xFF000000),
)

enum class AppTheme {
    SYSTEM,
    MYSTICAL,
    OCEAN,
    FOREST,
    SUNSET,
    LAVENDER,
    NEON,
    COSMIC,
    CHERRY_BLOSSOM,
    FIRE,
    ICE,
    EARTH,
    ROYAL,
    MINT
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
        AppTheme.NEON -> {
            if (darkTheme) NeonDarkColorScheme else NeonLightColorScheme
        }
        AppTheme.COSMIC -> {
            if (darkTheme) CosmicDarkColorScheme else CosmicLightColorScheme
        }
        AppTheme.CHERRY_BLOSSOM -> {
            if (darkTheme) CherryBlossomDarkColorScheme else CherryBlossomLightColorScheme
        }
        AppTheme.FIRE -> {
            if (darkTheme) FireDarkColorScheme else FireLightColorScheme
        }
        AppTheme.ICE -> {
            if (darkTheme) IceDarkColorScheme else IceLightColorScheme
        }
        AppTheme.EARTH -> {
            if (darkTheme) EarthDarkColorScheme else EarthLightColorScheme
        }
        AppTheme.ROYAL -> {
            if (darkTheme) RoyalDarkColorScheme else RoyalLightColorScheme
        }
        AppTheme.MINT -> {
            if (darkTheme) MintDarkColorScheme else MintLightColorScheme
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
    AppTheme.NEON -> "Neon Cyberpunk"
    AppTheme.COSMIC -> "Cosmic Space"
    AppTheme.CHERRY_BLOSSOM -> "Cherry Blossom"
    AppTheme.FIRE -> "Fire & Flame"
    AppTheme.ICE -> "Ice Crystal"
    AppTheme.EARTH -> "Earth & Wood"
    AppTheme.ROYAL -> "Royal Blue"
    AppTheme.MINT -> "Fresh Mint"
}

fun AppTheme.getIcon(): String = when (this) {
    AppTheme.SYSTEM -> "🎨"
    AppTheme.MYSTICAL -> "🔮"
    AppTheme.OCEAN -> "🌊"
    AppTheme.FOREST -> "🌲"
    AppTheme.SUNSET -> "🌅"
    AppTheme.LAVENDER -> "💜"
    AppTheme.NEON -> "⚡"
    AppTheme.COSMIC -> "🌌"
    AppTheme.CHERRY_BLOSSOM -> "🌸"
    AppTheme.FIRE -> "🔥"
    AppTheme.ICE -> "❄️"
    AppTheme.EARTH -> "🌍"
    AppTheme.ROYAL -> "👑"
    AppTheme.MINT -> "🌿"
}