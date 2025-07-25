package io.github.warforged5.mashkmp.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import io.github.warforged5.mashkmp.ui.theme.AppTheme
import io.github.warforged5.mashkmp.ui.theme.getDisplayName
import io.github.warforged5.mashkmp.ui.theme.getIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeOptionCard(
    theme: AppTheme,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else 1f,
        animationSpec = spring(
            dampingRatio = 0.6f,
            stiffness = Spring.StiffnessMedium
        )
    )

    ElevatedCard  (
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .scale(animatedScale),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),

        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Theme preview
            ThemePreviewCircles(theme = theme)

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        theme.getIcon(),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        theme.getDisplayName(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Text(
                    getThemeDescription(theme),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            AnimatedVisibility(
                visible = isSelected,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                Surface(
                    modifier = Modifier.size(32.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Rounded.Check,
                            contentDescription = "Selected",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ThemePreviewCircles(theme: AppTheme) {
    val colors = getThemePreviewColors(theme)

    Row(
        horizontalArrangement = Arrangement.spacedBy((-8).dp)
    ) {
        colors.forEachIndexed { index, color ->
            Surface(
                modifier = Modifier
                    .size(24.dp)
                    .zIndex((colors.size - index).toFloat()),
                shape = CircleShape,
                color = color,
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.surface)
            ) {}
        }
    }
}

fun getThemePreviewColors(theme: AppTheme): List<Color> = when (theme) {
    AppTheme.SYSTEM -> listOf(
        Color(0xFF6750A4),
        Color(0xFF625B71),
        Color(0xFF7D5260)
    )
    AppTheme.MYSTICAL -> listOf(
        Color(0xFF9C27B0),
        Color(0xFF673AB7),
        Color(0xFF8E24AA)
    )
    AppTheme.OCEAN -> listOf(
        Color(0xFF0077BE),
        Color(0xFF00BCD4),
        Color(0xFF0097A7)
    )
    AppTheme.FOREST -> listOf(
        Color(0xFF4CAF50),
        Color(0xFF8BC34A),
        Color(0xFF66BB6A)
    )
    AppTheme.SUNSET -> listOf(
        Color(0xFFFF5722),
        Color(0xFFE91E63),
        Color(0xFFFF9800)
    )
    AppTheme.LAVENDER -> listOf(
        Color(0xFFBA68C8),
        Color(0xFFCE93D8),
        Color(0xFFF48FB1)
    )
    AppTheme.NEON -> listOf(
        Color(0xFFFF0080),
        Color(0xFF00FF80),
        Color(0xFF0080FF)
    )
    AppTheme.COSMIC -> listOf(
        Color(0xFF8A2BE2),
        Color(0xFF483D8B),
        Color(0xFF9932CC)
    )
    AppTheme.CHERRY_BLOSSOM -> listOf(
        Color(0xFFFFB6C1),
        Color(0xFFFFC0CB),
        Color(0xFFEEB4B4)
    )
    AppTheme.FIRE -> listOf(
        Color(0xFFFF4500),
        Color(0xFFDC143C),
        Color(0xFFFF6347)
    )
    AppTheme.ICE -> listOf(
        Color(0xFF87CEEB),
        Color(0xFFB0E0E6),
        Color(0xFFE0F6FF)
    )
    AppTheme.EARTH -> listOf(
        Color(0xFFD2B48C),
        Color(0xFFCD853F),
        Color(0xFFBC9A6A)
    )
    AppTheme.ROYAL -> listOf(
        Color(0xFF4169E1),
        Color(0xFFFFD700),
        Color(0xFF800080)
    )
    AppTheme.MINT -> listOf(
        Color(0xFF98FB98),
        Color(0xFF7FFFD4),
        Color(0xFFAFEEEE)
    )
}

fun getThemeDescription(theme: AppTheme): String = when (theme) {
    AppTheme.SYSTEM -> "Follows your device's color theme"
    AppTheme.MYSTICAL -> "Deep purples for fortune telling vibes"
    AppTheme.OCEAN -> "Calming blues like the deep sea"
    AppTheme.FOREST -> "Natural greens for earth connection"
    AppTheme.SUNSET -> "Warm oranges and pinks like twilight"
    AppTheme.LAVENDER -> "Soft purples for dreamy experiences"
    AppTheme.NEON -> "Bright cyberpunk colors that pop"
    AppTheme.COSMIC -> "Deep space purples and stellar blues"
    AppTheme.CHERRY_BLOSSOM -> "Delicate pinks inspired by spring"
    AppTheme.FIRE -> "Intense reds and oranges like flames"
    AppTheme.ICE -> "Cool crystalline blues and whites"
    AppTheme.EARTH -> "Warm browns and tans from nature"
    AppTheme.ROYAL -> "Regal blues with golden accents"
    AppTheme.MINT -> "Fresh greens for a clean feeling"
}