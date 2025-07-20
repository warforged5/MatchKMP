package io.github.warforged5.mashkmp.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import io.github.warforged5.mash.Home
import io.github.warforged5.mash.MashViewModel
import io.github.warforged5.mash.navigateToHistory
import io.github.warforged5.mash.navigateToNewMash
import io.github.warforged5.mashkmp.dataclasses.CategoryData
import kotlinx.coroutines.delay
import kotlin.math.*
import kotlin.random.Random
import androidx.compose.foundation.layout.WindowInsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MashResultScreen(
    navController: NavController,
    viewModel: MashViewModel
) {
    val result = viewModel.history.firstOrNull() ?: return
    var animationPhase by remember { mutableStateOf(ResultAnimationPhase.COSMIC_LOADING) }
    var revealedResults by remember { mutableStateOf(0) }
    val haptic = LocalHapticFeedback.current

    // Cinematic animation sequence
    LaunchedEffect(Unit) {
        if (result.story == null && result.spiralCount == null) {
            // Dramatic revelation sequence for automatic results
            delay(500)
            animationPhase = ResultAnimationPhase.COSMIC_SWIRL
            delay(2000)
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)

            animationPhase = ResultAnimationPhase.REVELATION_BURST
            delay(1500)

            animationPhase = ResultAnimationPhase.RESULTS_CASCADE

            // Cascade reveal each result
            result.selections.entries.forEach { _ ->
                delay(800)
                revealedResults++
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }

            delay(1000)
            animationPhase = ResultAnimationPhase.FINAL_PRESENTATION
        } else {
            // Immediate reveal for AI stories or spiral results
            animationPhase = ResultAnimationPhase.FINAL_PRESENTATION
            revealedResults = result.selections.size
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    AnimatedContent(
                        targetState = animationPhase,
                        transitionSpec = {
                            fadeIn(tween(500)) togetherWith fadeOut(tween(300))
                        }
                    ) { phase ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                when (phase) {
                                    ResultAnimationPhase.COSMIC_LOADING -> "Consulting the Cosmos"
                                    ResultAnimationPhase.COSMIC_SWIRL -> "The Vision Forms"
                                    ResultAnimationPhase.REVELATION_BURST -> "Destiny Revealed"
                                    else -> "Your Cosmic Destiny"
                                }
                            )
                            Text(
                                when (phase) {
                                    ResultAnimationPhase.COSMIC_LOADING -> "🌌"
                                    ResultAnimationPhase.COSMIC_SWIRL -> "🌀"
                                    ResultAnimationPhase.REVELATION_BURST -> "✨"
                                    else -> "🔮"
                                },
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Home) {
                            popUpTo<Home> { inclusive = true }
                        }
                    }) {
                        Icon(Icons.Rounded.Home, contentDescription = "Home")
                    }
                },

            )
        },
     contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f),
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.05f)
                        ),
                        radius = 1500f
                    )
                )
        ) {
            // Cosmic particle effects
            when (animationPhase) {
                ResultAnimationPhase.COSMIC_LOADING -> CosmicLoadingEffect()
                ResultAnimationPhase.COSMIC_SWIRL -> CosmicSwirlEffect()
                ResultAnimationPhase.REVELATION_BURST -> RevelationBurstEffect()
                else -> Unit
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Main cosmic orb/crystal ball during loading phases
                if (animationPhase in listOf(
                        ResultAnimationPhase.COSMIC_LOADING,
                        ResultAnimationPhase.COSMIC_SWIRL,
                        ResultAnimationPhase.REVELATION_BURST
                    )) {
                    item {
                        CosmicOrb(phase = animationPhase, colorP = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f))
                    }
                }

                // Dramatic title reveal
                if (animationPhase >= ResultAnimationPhase.RESULTS_CASCADE) {
                    item {
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(tween(1000)) + slideInVertically(tween(1000)) { it } + scaleIn(tween(1000))
                        ) {
                            CosmicTitleCard(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                        }
                    }
                }

                // AI Story with cosmic presentation
                if (result.story != null && animationPhase >= ResultAnimationPhase.RESULTS_CASCADE) {
                    item {
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(tween(1200, delayMillis = 500)) +
                                    slideInVertically(tween(1200, delayMillis = 500)) { it / 2 } +
                                    scaleIn(tween(1200, delayMillis = 500))
                        ) {
                            CosmicStoryCard(story = result.story)
                        }
                    }

                    item {
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(tween(800, delayMillis = 1000)) +
                                    slideInVertically(tween(800, delayMillis = 1000)) { -it / 3 }
                        ) {
                            Text(
                                "🌟 The Threads of Your Fate 🌟",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                // Cosmic result cards with cascade animation
                itemsIndexed(result.selections.entries.toList()) { index, (realCategoryName, selection) ->
                    val categoryData = result.template.categories.find { it.realName == realCategoryName }

                    if (categoryData != null) {
                        CosmicResultCard(
                            category = categoryData,
                            selection = selection,
                            isVisible = animationPhase >= ResultAnimationPhase.RESULTS_CASCADE && index < revealedResults,
                            delay = if (result.story == null && result.spiralCount == null) 0 else index * 300,
                            index = index,
                            totalResults = result.selections.size
                        )
                    }
                }

                // Action buttons with cosmic reveal
                if (animationPhase >= ResultAnimationPhase.FINAL_PRESENTATION) {
                    item {
                        AnimatedVisibility(
                            visible = revealedResults >= result.selections.size,
                            enter = fadeIn(tween(800, delayMillis = 500)) +
                                    slideInVertically(tween(800, delayMillis = 500)) { it } +
                                    scaleIn(tween(800, delayMillis = 500))
                        ) {
                            CosmicActionButtons(
                                onViewHistory = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                                    navController.navigateToHistory()
                                },
                                onDivineAgain = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    navController.navigateToNewMash()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CosmicOrb(phase: ResultAnimationPhase, colorP: Color) {
    val infiniteTransition = rememberInfiniteTransition()

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing)
        )
    )

    val innerRotation by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing)
        )
    )

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val orbSize by animateFloatAsState(
        targetValue = when (phase) {
            ResultAnimationPhase.COSMIC_LOADING -> 180f
            ResultAnimationPhase.COSMIC_SWIRL -> 220f
            ResultAnimationPhase.REVELATION_BURST -> 160f
            else -> 140f
        },
        animationSpec = spring(
            dampingRatio = 0.6f,
            stiffness = Spring.StiffnessMedium
        )
    )

    Box(
        modifier = Modifier.size(300.dp),
        contentAlignment = Alignment.Center
    ) {
        // Outer cosmic ring
        Canvas(
            modifier = Modifier
                .size(orbSize.dp * 1.5f)
                .rotate(rotation)
                .scale(scale)
        ) {
            drawCosmicRing(phase)
        }

        // Inner cosmic ring
        Canvas(
            modifier = Modifier
                .size(orbSize.dp * 1.2f)
                .rotate(innerRotation)
                .scale(scale * 0.8f)
        ) {
            drawInnerCosmicRing(phase, colorP = colorP)
        }

        // Central orb
        Surface(
            modifier = Modifier
                .size(orbSize.dp)
                .scale(scale),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f),
            shadowElevation = 0.dp
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.4f),
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                                Color.Transparent
                            ),
                            radius = orbSize * 0.8f
                        )
                    )
            ) {
                Text(
                    when (phase) {
                        ResultAnimationPhase.COSMIC_LOADING -> "🌌"
                        ResultAnimationPhase.COSMIC_SWIRL -> "🌀"
                        ResultAnimationPhase.REVELATION_BURST -> "✨"
                        else -> "🔮"
                    },
                    style = MaterialTheme.typography.displayLarge,
                    fontSize = (orbSize * 0.3f).sp
                )
            }
        }

        // Phase-specific text
        Column(
            modifier = Modifier.offset(y = (orbSize + 40).dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                when (phase) {
                    ResultAnimationPhase.COSMIC_LOADING -> "Accessing the cosmic database..."
                    ResultAnimationPhase.COSMIC_SWIRL -> "The energies converge and swirl..."
                    ResultAnimationPhase.REVELATION_BURST -> "The vision crystallizes before you!"
                    else -> "Your destiny awaits..."
                },
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

private fun DrawScope.drawCosmicRing(phase: ResultAnimationPhase) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    val radius = size.width / 3

    val colors = when (phase) {
        ResultAnimationPhase.COSMIC_LOADING -> listOf(
            Color(0xFF6750A4),
            Color(0xFF9C27B0),
            Color(0xFF673AB7)
        )
        ResultAnimationPhase.COSMIC_SWIRL -> listOf(
            Color(0xFF0077BE),
            Color(0xFF00BCD4),
            Color(0xFF6750A4)
        )
        ResultAnimationPhase.REVELATION_BURST -> listOf(
            Color(0xFFFF5722),
            Color(0xFFE91E63),
            Color(0xFF9C27B0)
        )
        else -> listOf(Color(0xFF6750A4))
    }

    repeat(3) { ring ->
        val ringRadius = radius + (ring * 20)
        val strokeWidth = (8 - ring * 2).dp.toPx()

        drawCircle(
            brush = Brush.sweepGradient(colors),
            radius = ringRadius,
            center = Offset(centerX, centerY),
            style = androidx.compose.ui.graphics.drawscope.Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round
            ),
            alpha = 0.6f - (ring * 0.2f)
        )
    }
}

private fun DrawScope.drawInnerCosmicRing(phase: ResultAnimationPhase, colorP: Color) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    val points = 6

    repeat(points) { i ->
        val angle = (2 * PI * i / points).toFloat()
        val radius = size.width / 4
        val x = centerX + cos(angle) * radius
        val y = centerY + sin(angle) * radius

        drawCircle(
            color = colorP,
            radius = 4.dp.toPx(),
            center = Offset(x, y)
        )
    }
}

@Composable
private fun CosmicTitleCard(colorP: Color) {
    Surface(
        shape = RoundedCornerShape(32.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
        shadowElevation = 0.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box {
            // Animated background
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.Transparent,
                            colorP,
                            Color.Transparent
                        )
                    )
                )
            }

            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,


                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(

                    "The Cosmic Revelation",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )

                    Text(
                        "The universe has spoken through the ancient art of divination",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )

            }
        }
    }
}

@Composable
private fun CosmicStoryCard(story: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
        shadowElevation = 0.dp
    ) {
        Box {
            // Cosmic background effect
            Canvas(modifier = Modifier.fillMaxSize()) {
                repeat(20) {
                    val x = Random.nextFloat() * size.width
                    val y = Random.nextFloat() * size.height
                    drawCircle(
                        color = Color.White.copy(alpha = 0.1f),
                        radius = 2.dp.toPx(),
                        center = Offset(x, y)
                    )
                }
            }

            Column(
                modifier = Modifier.padding(32.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(56.dp),
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                "📜",
                                style = MaterialTheme.typography.headlineLarge
                            )
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Your Cosmic Chronicle",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Written in the stars by ancient algorithms",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }

                Text(
                    story,
                    style = MaterialTheme.typography.bodyLarge,
                    lineHeight = 32.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Composable
private fun CosmicResultCard(
    category: CategoryData,
    selection: String,
    isVisible: Boolean,
    delay: Int,
    index: Int,
    totalResults: Int
) {
    val animatedScale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = spring(
            dampingRatio = 0.4f,
            stiffness = Spring.StiffnessLow
        )
    )

    val animatedRotation by animateFloatAsState(
        targetValue = if (isVisible) 0f else 180f,
        animationSpec = tween(800, delay)
    )

    val shimmerOffset by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(1000, delay + 200)
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .scale(animatedScale)
            .rotate(animatedRotation),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f),
        shadowElevation = 0.dp
    ) {
        Box {
            // Cosmic shimmer effect
            Canvas(modifier = Modifier.fillMaxSize()) {
                val shimmerX = size.width * shimmerOffset
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.White.copy(alpha = 0.3f),
                            Color.Transparent
                        ),
                        start = Offset(shimmerX - 100, 0f),
                        end = Offset(shimmerX + 100, size.height)
                    )
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Cosmic icon container
                Surface(
                    modifier = Modifier.size(72.dp),
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            category.icon,
                            style = MaterialTheme.typography.headlineLarge
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        category.nickname,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
                    )
                    Text(
                        selection,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }

                // Cosmic order indicator
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            "✨",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CosmicActionButtons(
    onViewHistory: () -> Unit,
    onDivineAgain: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = onViewHistory,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
            ) {
                Icon(Icons.Rounded.History, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cosmic Archive", fontWeight = FontWeight.SemiBold)
            }

            Button(
                onClick = onDivineAgain,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(Icons.Rounded.AutoAwesome, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Divine Again", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

// Particle effects for different phases
@Composable
private fun CosmicLoadingEffect() {
    val infiniteTransition = rememberInfiniteTransition()

    repeat(12) { index ->
        val delay = index * 100
        val animationDuration = 3000 + Random.nextInt(1000)

        val x by infiniteTransition.animateFloat(
            initialValue = Random.nextFloat() * 400,
            targetValue = Random.nextFloat() * 400,
            animationSpec = infiniteRepeatable(
                animation = tween(animationDuration, delayMillis = delay),
                repeatMode = RepeatMode.Reverse
            )
        )

        val y by infiniteTransition.animateFloat(
            initialValue = Random.nextFloat() * 800,
            targetValue = Random.nextFloat() * 800,
            animationSpec = infiniteRepeatable(
                animation = tween(animationDuration + 500, delayMillis = delay),
                repeatMode = RepeatMode.Reverse
            )
        )

        val alpha by infiniteTransition.animateFloat(
            initialValue = 0.2f,
            targetValue = 0.8f,
            animationSpec = infiniteRepeatable(
                animation = tween(2000, delayMillis = delay),
                repeatMode = RepeatMode.Reverse
            )
        )

        Text(
            listOf("✨", "⭐", "💫", "🌟")[index % 4],
            modifier = Modifier
                .offset(x.dp, y.dp)
                .alpha(alpha),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun CosmicSwirlEffect() {
    val infiniteTransition = rememberInfiniteTransition()

    repeat(16) { index ->
        val angle by infiniteTransition.animateFloat(
            initialValue = (index * 22.5f),
            targetValue = (index * 22.5f) + 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(4000 + (index * 100), easing = LinearEasing)
            )
        )

        val radius by infiniteTransition.animateFloat(
            initialValue = 50f + (index * 15f),
            targetValue = 100f + (index * 15f),
            animationSpec = infiniteRepeatable(
                animation = tween(2000, delayMillis = index * 50),
                repeatMode = RepeatMode.Reverse
            )
        )

        val centerX = 200.dp
        val centerY = 400.dp

        val x = centerX + cos(angle * PI / 180).dp * radius
        val y = centerY + sin(angle * PI / 180).dp * radius

        Text(
            "🌀",
            modifier = Modifier.offset(x, y),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
        )
    }
}

@Composable
private fun RevelationBurstEffect() {
    val infiniteTransition = rememberInfiniteTransition()

    repeat(24) { index ->
        val scale by infiniteTransition.animateFloat(
            initialValue = 0.5f,
            targetValue = 2f,
            animationSpec = infiniteRepeatable(
                animation = tween(1500, delayMillis = index * 50),
                repeatMode = RepeatMode.Restart
            )
        )

        val alpha by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = tween(1500, delayMillis = index * 50),
                repeatMode = RepeatMode.Restart
            )
        )

        val angle = (index * 15f) * PI / 180
        val distance = 150

        val x = (200.0 + cos(angle) * distance).dp
        val y = (400.0 + sin(angle) * distance).dp

        Text(
            "✨",
            modifier = Modifier
                .offset(x, y)
                .scale(scale)
                .alpha(alpha),
            style = MaterialTheme.typography.titleLarge
        )
    }
}



enum class ResultAnimationPhase {
    COSMIC_LOADING,
    COSMIC_SWIRL,
    REVELATION_BURST,
    RESULTS_CASCADE,
    FINAL_PRESENTATION
}