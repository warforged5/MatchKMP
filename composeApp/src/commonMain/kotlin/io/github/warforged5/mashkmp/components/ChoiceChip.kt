package io.github.warforged5.mashkmp.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChoiceChip(
    choice: String,
    index: Int,
    onRemove: () -> Unit
) {
    val animatedScale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = 0.6f,
            stiffness = Spring.StiffnessLow
        )
    )

    var isPressed by remember { mutableStateOf(false) }

    ElevatedCard(
        onClick = { },
        modifier = Modifier
            .fillMaxWidth()
            .scale(animatedScale),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 2.dp
        ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(24.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.secondary
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "$index",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Text(
                    choice,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            IconButton(
                onClick = {
                    isPressed = true
                    onRemove()
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(32.dp)
            ) {
                Icon(
                    Icons.Rounded.Close,
                    contentDescription = "Remove",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.6f),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun NewChoiceChip(
    choice: String,
    isEliminated: Boolean,
    isFinal: Boolean,
    isBeingCounted: Boolean
) {
    // Pulse animation for counting
    val infiniteTransition = rememberInfiniteTransition()
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = when {
            isFinal -> MaterialTheme.colorScheme.primary
            isBeingCounted -> MaterialTheme.colorScheme.secondary
            isEliminated -> MaterialTheme.colorScheme.error
            else -> MaterialTheme.colorScheme.secondaryContainer
        },
        border = when {
            isFinal -> BorderStroke(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = glowAlpha))
            isBeingCounted -> BorderStroke(3.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f))
            else -> null
        },
        shadowElevation = when {
            isFinal -> 8.dp
            isBeingCounted -> 6.dp
            else -> 2.dp
        },
        modifier = Modifier.scale(if (isBeingCounted) pulseScale else 1f)
    ) {
        Box {
            // Extra glow for counting
            if (isBeingCounted) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                                    Color.Transparent
                                ),
                                radius = 100f
                            )
                        )
                )
            }

            Text(
                choice,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = when {
                    isFinal -> MaterialTheme.colorScheme.onPrimary
                    isBeingCounted -> MaterialTheme.colorScheme.onSecondary
                    isEliminated -> MaterialTheme.colorScheme.onError
                    else -> MaterialTheme.colorScheme.onSecondaryContainer
                },
                fontWeight = when {
                    isFinal -> FontWeight.Bold
                    isBeingCounted -> FontWeight.SemiBold
                    else -> FontWeight.Medium
                }
            )
        }
    }
}

internal fun getCategoryEmoji(categoryName: String): String = when (categoryName) {
    "MASH" -> "🏠"
    "People" -> "💖"
    "Number" -> "👶"
    "Vehicle" -> "🚗"
    "Places" -> "🌍"
    "Occupations" -> "💼"
    "Color" -> "🎨"
    "Flavor" -> "🍎"
    "Pet" -> "🐕"
    "Vacation Spot" -> "✈️"
    "Hobby" -> "🎯"
    "Best Friend" -> "👫"
    "College/School" -> "🎓"
    "Wedding Theme" -> "💒"
    "First Date" -> "💕"
    "Honeymoon" -> "🌴"
    "Salary" -> "💰"
    "House Color" -> "🏠"
    "Age When Married" -> "💍"
    "Anniversary Gift" -> "🎁"
    "Lucky Number" -> "🔢"
    "Dream Destination" -> "🗺️"
    "Favorite Food" -> "🍕"
    "Music Genre" -> "🎵"
    "Weather" -> "☀️"
    "Season" -> "🍂"
    else -> "⭐"
}