package io.github.warforged5.mashkmp.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import io.github.warforged5.mash.navigateToCreateTemplate
import io.github.warforged5.mash.navigateToHistory
import io.github.warforged5.mash.navigateToNewMash
import io.github.warforged5.mashkmp.components.AnimatedHomeCard
import io.github.warforged5.mashkmp.components.ThemeSelectionDialog
import io.github.warforged5.mash.ui.theme.ThemeManager
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.ui.graphics.vector.ImageVector
import io.github.warforged5.mash.navigateToMashInput
import io.github.warforged5.mashkmp.MashViewModel
import kotlinx.datetime.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.math.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun HomeScreen(
    navController: NavController,
    themeManager: ThemeManager,
    viewModel: MashViewModel
) {
    val haptic = LocalHapticFeedback.current
    var showThemeDialog by remember { mutableStateOf(false) }

    // Get current time for greeting
    val currentHour = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).hour
    val greeting = when (currentHour) {
        in 0..11 -> "Good Morning"
        in 12..17 -> "Good Afternoon"
        else -> "Good Evening"
    }

    // Get stats
    val totalGamesPlayed = viewModel.history.size
    val mostRecentGame = viewModel.history.firstOrNull()
    val featuredTemplates = viewModel.getFeaturedTemplates()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 75, 0, 0)
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Animated background gradient
            AnimatedBackground()

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                // Modern Header Section
                item {
                    ModernHeaderSection(
                        greeting = greeting,
                        onThemeClick = { showThemeDialog = true }
                    )
                }

                // Stats Dashboard
                if (totalGamesPlayed > 0) {
                    item {
                        StatsDashboard(
                            totalGames = totalGamesPlayed,
                            recentGame = mostRecentGame?.template?.name ?: "Unknown",
                            favoriteCategory = getMostUsedCategory(viewModel)
                        )
                    }
                }

                // Quick Actions
                item {
                    QuickActionsSection(
                        onPlayClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            navController.navigateToNewMash()
                        },
                        onTemplatesClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            navController.navigateToCreateTemplate()
                        },
                        onHistoryClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            navController.navigateToHistory()
                        }
                    )
                }

                // Featured Templates
                if (featuredTemplates.isNotEmpty()) {
                    item {
                        FeaturedTemplatesSection(
                            templates = featuredTemplates,
                            onTemplateClick = { template ->
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                navController.navigateToMashInput(template.id)
                            }
                        )
                    }
                }

                // Recent Activity
                if (viewModel.history.isNotEmpty()) {
                    item {
                        RecentActivitySection(
                            recentGames = viewModel.history.take(3),
                            onSeeAllClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                navController.navigateToHistory()
                            }
                        )
                    }
                }
            }

            // Floating Action Button
            ExtendedFloatingActionButton(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    navController.navigateToNewMash()
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Rounded.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("New Game", fontWeight = FontWeight.SemiBold)
            }
        }
    }

    if (showThemeDialog) {
        ThemeSelectionDialog(
            currentTheme = themeManager.currentTheme,
            onThemeSelected = { theme ->
                themeManager.setTheme(theme)
            },
            currentDarkMode = themeManager.darkModePreference,
            onDarkModeSelected = { preference ->
                themeManager.setDarkModePreference(preference)
            },
            onDismiss = { showThemeDialog = false }
        )
    }
}

@Composable
private fun AnimatedBackground() {
    val infiniteTransition = rememberInfiniteTransition()

    val offsetX by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(25000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        // Draw animated gradient circles
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF6750A4).copy(alpha = 0.1f),
                    Color.Transparent
                ),
                radius = 500f
            ),
            radius = 500f,
            center = Offset(offsetX, offsetY)
        )

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF9C27B0).copy(alpha = 0.08f),
                    Color.Transparent
                ),
                radius = 400f
            ),
            radius = 400f,
            center = Offset(size.width - offsetX, size.height - offsetY)
        )
    }
}

@Composable
private fun ModernHeaderSection(
    greeting: String,
    onThemeClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 40.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        greeting,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "MASH",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "Discover Your Future",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                IconButton(
                    onClick = onThemeClick,
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                            CircleShape
                        )
                ) {
                    Icon(
                        Icons.Rounded.Palette,
                        contentDescription = "Theme",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun StatsDashboard(
    totalGames: Int,
    recentGame: String,
    favoriteCategory: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(
                icon = Icons.Rounded.Games,
                value = totalGames.toString(),
                label = "Games Played",
                color = MaterialTheme.colorScheme.primary
            )

            StatItem(
                icon = Icons.Rounded.Star,
                value = favoriteCategory,
                label = "Favorite",
                color = MaterialTheme.colorScheme.secondary
            )

            StatItem(
                icon = Icons.Rounded.TrendingUp,
                value = "${(totalGames * 4.2).toInt()}",
                label = "Futures Seen",
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Composable
private fun StatItem(
    icon: ImageVector,
    value: String,
    label: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(
            modifier = Modifier.size(48.dp),
            shape = CircleShape,
            color = color.copy(alpha = 0.2f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Text(
            value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun QuickActionsSection(
    onPlayClick: () -> Unit,
    onTemplatesClick: () -> Unit,
    onHistoryClick: () -> Unit
) {
    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Quick Actions",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                QuickActionCard(
                    title = "Play Now",
                    subtitle = "Start a new game",
                    icon = Icons.Rounded.PlayArrow,
                    gradient = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                    ),
                    onClick = onPlayClick
                )
            }

            item {
                QuickActionCard(
                    title = "Templates",
                    subtitle = "Browse & create",
                    icon = Icons.Rounded.Dashboard,
                    gradient = listOf(
                        MaterialTheme.colorScheme.secondary,
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)
                    ),
                    onClick = onTemplatesClick
                )
            }

            item {
                QuickActionCard(
                    title = "History",
                    subtitle = "Your past futures",
                    icon = Icons.Rounded.History,
                    gradient = listOf(
                        MaterialTheme.colorScheme.tertiary,
                        MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f)
                    ),
                    onClick = onHistoryClick
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuickActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    gradient: List<Color>,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier
            .width(140.dp)
            .height(140.dp),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 2.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = gradient,
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = Color.White
                )

                Column {
                    Text(
                        title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }
    }
}

@Composable
private fun FeaturedTemplatesSection(
    templates: List<io.github.warforged5.mashkmp.dataclasses.MashTemplate>,
    onTemplateClick: (io.github.warforged5.mashkmp.dataclasses.MashTemplate) -> Unit
) {
    Column(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Rounded.AutoAwesome,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    "Featured Templates",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(templates) { template ->
                FeaturedTemplateCard(
                    template = template,
                    onClick = { onTemplateClick(template) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FeaturedTemplateCard(
    template: io.github.warforged5.mashkmp.dataclasses.MashTemplate,
    onClick: () -> Unit
) {
   Card(
        onClick = onClick,
        modifier = Modifier
            .width(160.dp)
            .height(120.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    template.categories.firstOrNull()?.icon ?: "🎯",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    template.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "${template.categories.size} categories",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Icon(
                    Icons.Rounded.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun RecentActivitySection(
    recentGames: List<io.github.warforged5.mashkmp.dataclasses.MashResult>,
    onSeeAllClick: () -> Unit
) {
    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Recent Activity",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            TextButton(onClick = onSeeAllClick) {
                Text("See All")
                Icon(
                    Icons.Rounded.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier
                        .size(16.dp)
                        .padding(start = 4.dp)
                )
            }
        }

        recentGames.forEach { result ->
            RecentGameCard(result)
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
private fun RecentGameCard(
    result: io.github.warforged5.mashkmp.dataclasses.MashResult
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        result.template.categories.firstOrNull()?.icon ?: "🎮",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    result.template.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )

                val instant = Instant.fromEpochMilliseconds(result.timestamp)
                val now = Clock.System.now()
                val duration = now - instant

                Text(
                    when {
                        duration.inWholeHours < 1 -> "Just now"
                        duration.inWholeHours < 24 -> "${duration.inWholeHours}h ago"
                        duration.inWholeDays < 7 -> "${duration.inWholeDays}d ago"
                        else -> "Last week"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (result.story != null) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                ) {
                    Text(
                        "AI",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

private fun getMostUsedCategory(viewModel: MashViewModel): String {
    val categoryCount = mutableMapOf<String, Int>()

    viewModel.history.forEach { result ->
        result.selections.keys.forEach { category ->
            categoryCount[category] = (categoryCount[category] ?: 0) + 1
        }
    }

    return categoryCount.maxByOrNull { it.value }?.key?.take(10) ?: "None"
}