package io.github.warforged5.mashkmp.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Dialog
import io.github.warforged5.mashkmp.enumclasses.SpiralPhase
import kotlinx.coroutines.delay
import kotlin.math.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun NewSpiralDrawingDialog(
    onCountComplete: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var phase by remember { mutableStateOf(SpiralPhase.READY) }
    var spiralData by remember { mutableStateOf(SpiralDrawingData()) }
    var ringCount by remember { mutableStateOf("") }
    var showInstructions by remember { mutableStateOf(true) }
    val haptic = LocalHapticFeedback.current

    // Enhanced physics simulation with continuous looping
    LaunchedEffect(phase) {
        when (phase) {
            SpiralPhase.DRAWING -> {
                val startTime = Clock.System.now().toEpochMilliseconds()
                while (phase == SpiralPhase.DRAWING) {
                    val elapsed = Clock.System.now().toEpochMilliseconds() - startTime
                    // Make spiral run longer (12 seconds) and loop continuously
                    val cycleDuration = 12000f // 12 seconds per cycle
                    val rawProgress = (elapsed % cycleDuration.toLong()) / cycleDuration

                    spiralData = spiralData.copy(
                        automaticProgress = rawProgress,
                        isDrawingComplete = false // Never auto-complete
                    )

                    delay(16) // ~60fps
                }
            }
            else -> { /* Do nothing */ }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.95f),
            shape = RoundedCornerShape(32.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 16.dp
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                // Cosmic background with swirling energy
                CosmicSpiralBackground(
                    phase = phase,
                    progress = spiralData.automaticProgress
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    when (phase) {
                        SpiralPhase.READY -> {
                            EnhancedReadyToDrawContent(
                                showInstructions = showInstructions,
                                onToggleInstructions = { showInstructions = !showInstructions },
                                onStartDrawing = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    phase = SpiralPhase.DRAWING
                                }
                            )
                        }

                        SpiralPhase.DRAWING -> {
                            EnhancedDrawingContent(
                                spiralData = spiralData,
                                onStop = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    spiralData = spiralData.copy(
                                        stoppedProgress = spiralData.automaticProgress
                                    )
                                    phase = SpiralPhase.STOPPED
                                }
                            )
                        }

                        SpiralPhase.STOPPED -> {
                            EnhancedStoppedContent(
                                spiralData = spiralData,
                                onRedraw = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    spiralData = SpiralDrawingData()
                                    phase = SpiralPhase.DRAWING
                                },
                                onContinue = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    phase = SpiralPhase.COUNTING
                                }
                            )
                        }

                        SpiralPhase.COUNTING -> {
                            EnhancedCountingContent(
                                spiralData = spiralData,
                                ringCount = ringCount,
                                onRingCountChange = { ringCount = it },
                                onComplete = { count ->
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onCountComplete(count)
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
private fun EnhancedReadyToDrawContent(
    showInstructions: Boolean,
    onToggleInstructions: () -> Unit,
    onStartDrawing: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        // Animated cosmic orb
        CosmicReadyOrb()

        // Title with cosmic effects
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Ready to Channel Cosmic Energy?",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )

            AnimatedVisibility(
                visible = showInstructions,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                InstructionsCard(onToggle = onToggleInstructions)
            }
        }

        // Enhanced start button
        CosmicStartButton(
            onClick = onStartDrawing,
            onToggleInstructions = onToggleInstructions,
            showInstructions = showInstructions
        )
    }
}

@Composable
private fun CosmicReadyOrb() {
    val infiniteTransition = rememberInfiniteTransition()

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing)
        )
    )

    val innerRotation by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing)
        )
    )

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier.size(160.dp),
        contentAlignment = Alignment.Center
    ) {
        // Outer energy ring
        Canvas(
            modifier = Modifier
                .size(160.dp)
                .rotate(rotation)
        ) {
            drawCosmicEnergyRing(0.8f)
        }

        // Inner energy ring
        Canvas(
            modifier = Modifier
                .size(120.dp)
                .rotate(innerRotation)
        ) {
            drawCosmicEnergyRing(0.6f)
        }

        // Central orb
        Surface(
            modifier = Modifier
                .size(80.dp)
                .scale(pulseScale),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            shadowElevation = 16.dp
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.4f),
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                Color.Transparent
                            )
                        )
                    )
            ) {
                Text(
                    "🌀",
                    style = MaterialTheme.typography.displayMedium
                )
            }
        }
    }
}

private fun DrawScope.drawCosmicEnergyRing(alpha: Float) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    val radius = size.width / 3

    repeat(6) { i ->
        val angle = (i * 60f) * PI / 180f
        val startAngle = angle.toFloat() * 180f / PI.toFloat()

        drawArc(
            brush = Brush.sweepGradient(
                colors = listOf(
                    Color.Transparent,
                    Color(0xFF6750A4).copy(alpha = alpha),
                    Color(0xFF9C27B0).copy(alpha = alpha),
                    Color.Transparent
                )
            ),
            startAngle = startAngle,
            sweepAngle = 30f,
            useCenter = false,
            style = Stroke(
                width = 4.dp.toPx(),
                cap = StrokeCap.Round
            )
        )
    }
}

@Composable
private fun InstructionsCard(onToggle: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "How It Works",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                IconButton(onClick = onToggle) {
                    Icon(
                        Icons.Rounded.ExpandLess,
                        contentDescription = "Collapse",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InstructionStep(
                    number = "1",
                    text = "Press START to begin the cosmic energy flow"
                )
                InstructionStep(
                    number = "2",
                    text = "Watch the mystical spiral flow continuously in cycles"
                )
                InstructionStep(
                    number = "3",
                    text = "Press STOP when your intuition tells you to"
                )
                InstructionStep(
                    number = "4",
                    text = "Count the rings in the spiral to determine fate"
                )
            }
        }
    }
}

@Composable
private fun InstructionStep(number: String, text: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(28.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.secondary
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    number,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        }

        Text(
            text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )
    }
}

@Composable
private fun CosmicStartButton(
    onClick: () -> Unit,
    onToggleInstructions: () -> Unit,
    showInstructions: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val infiniteTransition = rememberInfiniteTransition()

        val buttonGlow by infiniteTransition.animateFloat(
            initialValue = 0.6f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1500, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )

        Surface(
            onClick = onClick,
            modifier = Modifier.size(140.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary,
            shadowElevation = 16.dp,
            border = BorderStroke(
                3.dp,
                MaterialTheme.colorScheme.primary.copy(alpha = buttonGlow)
            )
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.2f),
                                Color.Transparent
                            )
                        )
                    )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Rounded.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

        TextButton(onClick = onToggleInstructions) {
            Icon(
                if (showInstructions) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(if (showInstructions) "Hide Instructions" else "Show Instructions")
        }
    }
}

@Composable
private fun EnhancedDrawingContent(
    spiralData: SpiralDrawingData,
    onStop: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Status indicator
        EnhancedDrawingStatus(
            progress = spiralData.automaticProgress
        )

        // Interactive drawing canvas (no user interaction)
        AutomaticSpiralCanvas(
            automaticProgress = spiralData.automaticProgress
        )

        // Enhanced stop button
        CosmicStopButton(onStop = onStop)

        // Progress feedback (simplified) - now shows cycle info
        ProgressFeedback(
            progress = spiralData.automaticProgress
        )
    }
}

@OptIn(ExperimentalTime::class)
@Composable
private fun EnhancedDrawingStatus(
    progress: Float
) {
    val cycleDuration = 12000L // Match the cycle duration
    val cycleNumber = (Clock.System.now().toEpochMilliseconds() / cycleDuration).toInt() + 1

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "🌀 Channeling Cosmic Energy 🌀",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun AutomaticSpiralCanvas(
    automaticProgress: Float
) {
    Surface(
        modifier = Modifier.size(320.dp),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Draw automatic spiral only
            drawEnhancedSpiral(
                progress = automaticProgress,
                color = Color(0xFF6750A4),
                strokeWidth = 4.dp.toPx(),
                style = SpiralStyle.AUTOMATIC
            )

            // Draw energy particles along spiral
            drawSpiralEnergyParticles(automaticProgress)
        }
    }
}

private fun DrawScope.drawEnhancedSpiral(
    progress: Float,
    color: Color,
    strokeWidth: Float,
    style: SpiralStyle
) {
    val center = Offset(size.width / 2, size.height / 2)
    val maxRadius = minOf(size.width, size.height) / 2 - strokeWidth
    val path = Path()

    // Calculate points based on continuous progress (can exceed 1.0)
    val effectiveProgress = progress
    val totalPoints = (effectiveProgress * 1200).toInt() // More points for smoother spiral
    var isFirst = true

    for (i in 0 until totalPoints) {
        val t = i / 1200f * effectiveProgress
        val angle = t * 18f * PI.toFloat() // More revolutions for longer spiral
        val radius = (t * maxRadius).coerceAtMost(maxRadius) // Cap at max radius

        // Add some organic variation
        val variation = sin(t * 30f) * 3f
        val finalRadius = radius + variation

        val x = center.x + finalRadius * cos(angle)
        val y = center.y + finalRadius * sin(angle)

        if (isFirst) {
            path.moveTo(x, y)
            isFirst = false
        } else {
            path.lineTo(x, y)
        }
    }

    drawPath(
        path = path,
        color = color,
        style = Stroke(
            width = strokeWidth,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )
}

private fun DrawScope.drawSpiralEnergyParticles(progress: Float) {
    val center = Offset(size.width / 2, size.height / 2)
    val maxRadius = minOf(size.width, size.height) / 2

    val particleCount = (progress * 50).toInt()
    repeat(particleCount) { i ->
        val t = i / 50f * progress
        val angle = t * 18f * PI.toFloat()
        val radius = t * maxRadius

        val x = center.x + radius * cos(angle)
        val y = center.y + radius * sin(angle)

        val alpha = (1f - i.toFloat() / particleCount) * 0.8f
        drawCircle(
            color = Color.White.copy(alpha = alpha),
            radius = 2.dp.toPx(),
            center = Offset(x, y)
        )
    }
}

@Composable
private fun CosmicStopButton(onStop: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition()

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val glowIntensity by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Surface(
        onClick = onStop,
        modifier = Modifier
            .size(120.dp)
            .scale(pulseScale),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.error,
        shadowElevation = 12.dp,
        border = BorderStroke(
            3.dp,
            MaterialTheme.colorScheme.error.copy(alpha = glowIntensity)
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    )
                )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Rounded.Stop,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp),
                    tint = MaterialTheme.colorScheme.onError
                )
                Text(
                    "STOP",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onError
                )
            }
        }
    }
}

@Composable
private fun ProgressFeedback(
    progress: Float
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
    ) {
        Text(
            "Energy: ${(progress * 100).toInt()}% ",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun EnhancedStoppedContent(
    spiralData: SpiralDrawingData,
    onRedraw: () -> Unit,
    onContinue: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Dramatic stop announcement
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "⚡ COSMIC ENERGY CAPTURED! ⚡",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.error
                )

                Text(
                    "Your spiral holds the key to your destiny",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }

        // Final spiral display
        FinalSpiralDisplay(spiralData = spiralData)

        // Action buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = onRedraw,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Rounded.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Redraw")
            }

            Button(
                onClick = onContinue,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Rounded.Visibility, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Count")
            }
        }
    }
}

@Composable
private fun FinalSpiralDisplay(spiralData: SpiralDrawingData) {
    Surface(
        modifier = Modifier.size(280.dp),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        border = BorderStroke(3.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Draw the stopped spiral
            drawEnhancedSpiral(
                progress = spiralData.stoppedProgress,
                color = Color(0xFF6750A4),
                strokeWidth = 4.dp.toPx(),
                style = SpiralStyle.AUTOMATIC
            )

            // Add mystical sparkles at stop point
            drawMysticalEffects(spiralData.stoppedProgress)
        }
    }
}

private fun DrawScope.drawMysticalEffects(stoppedProgress: Float) {
    val center = Offset(size.width / 2, size.height / 2)
    val maxRadius = minOf(size.width, size.height) / 2
    val angle = stoppedProgress * 18f * PI.toFloat()
    val radius = stoppedProgress * maxRadius

    val stopX = center.x + radius * cos(angle)
    val stopY = center.y + radius * sin(angle)

    // Draw sparkle effect at stop point
    drawCircle(
        color = Color.Yellow.copy(alpha = 0.8f),
        radius = 8.dp.toPx(),
        center = Offset(stopX, stopY)
    )

    drawCircle(
        color = Color.White.copy(alpha = 0.6f),
        radius = 4.dp.toPx(),
        center = Offset(stopX, stopY)
    )
}

@Composable
private fun EnhancedCountingContent(
    spiralData: SpiralDrawingData,
    ringCount: String,
    onRingCountChange: (String) -> Unit,
    onComplete: (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Count the Cosmic Rings",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    "Each complete ring holds a piece of your destiny",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }

        // Spiral with counting overlay
        CountingSpiralDisplay(spiralData = spiralData)

        // Enhanced number input
        CosmicNumberInput(
            value = ringCount,
            onValueChange = onRingCountChange,
            onComplete = onComplete
        )
    }
}

@Composable
private fun CountingSpiralDisplay(spiralData: SpiralDrawingData) {
    Surface(
        modifier = Modifier.size(260.dp),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            // Draw the final spiral
            drawEnhancedSpiral(
                progress = spiralData.stoppedProgress,
                color = Color(0xFF6750A4),
                strokeWidth = 4.dp.toPx(),
                style = SpiralStyle.AUTOMATIC
            )

            // Add counting aids (ring markers)
            drawRingCountingAids(spiralData.stoppedProgress)
        }
    }
}

private fun DrawScope.drawRingCountingAids(stoppedProgress: Float) {
    val center = Offset(size.width / 2, size.height / 2)
    val maxRadius = minOf(size.width, size.height) / 2

    // Draw concentric circles to help with counting
    val ringCount = (stoppedProgress * 5).toInt().coerceAtLeast(1)
    repeat(ringCount) { ring ->
        val ringRadius = (ring + 1) * maxRadius / (ringCount + 1)
        drawCircle(
            color = Color.Gray.copy(alpha = 0.2f),
            radius = ringRadius,
            center = center,
            style = Stroke(
                width = 1.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f))
            )
        )
    }
}

@Composable
private fun CosmicNumberInput(
    value: String,
    onValueChange: (String) -> Unit,
    onComplete: (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() } && newValue.length <= 2) {
                    onValueChange(newValue)
                }
            },
            label = { Text("Number of rings") },
            placeholder = { Text("1-20") },
            singleLine = true,
            modifier = Modifier.width(180.dp),
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    val count = value.toIntOrNull()
                    if (count != null && count > 0) {
                        onComplete(count)
                    }
                }
            ),
            trailingIcon = {
                if (value.isNotBlank()) {
                    IconButton(
                        onClick = {
                            val count = value.toIntOrNull()
                            if (count != null && count > 0) {
                                onComplete(count)
                            }
                        }
                    ) {
                        Icon(Icons.Rounded.Check, contentDescription = "Confirm")
                    }
                }
            }
        )

        Button(
            onClick = {
                val count = value.toIntOrNull()
                if (count != null && count > 0) {
                    onComplete(count)
                }
            },
            enabled = value.toIntOrNull()?.let { it > 0 } == true,
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Icon(Icons.Rounded.AutoAwesome, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Begin Elimination", fontWeight = FontWeight.SemiBold)
        }

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
        ) {
            Text(
                "💫 Count complete loops",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun CosmicSpiralBackground(
    phase: SpiralPhase,
    progress: Float
) {
    val infiniteTransition = rememberInfiniteTransition()

    val backgroundAlpha by infiniteTransition.animateFloat(
        initialValue = 0.05f,
        targetValue = 0.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        // Draw cosmic background effect based on phase
        when (phase) {
            SpiralPhase.READY -> drawCosmicReadyBackground(backgroundAlpha)
            SpiralPhase.DRAWING -> drawCosmicDrawingBackground(progress, backgroundAlpha)
            SpiralPhase.STOPPED -> drawCosmicStoppedBackground(backgroundAlpha)
            SpiralPhase.COUNTING -> drawCosmicCountingBackground(backgroundAlpha)
        }
    }
}

private fun DrawScope.drawCosmicReadyBackground(alpha: Float) {
    drawRect(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFF6750A4).copy(alpha = alpha),
                Color.Transparent,
                Color(0xFF9C27B0).copy(alpha = alpha * 0.5f)
            ),
            radius = size.width
        )
    )
}

private fun DrawScope.drawCosmicDrawingBackground(progress: Float, alpha: Float) {
    drawRect(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFF0077BE).copy(alpha = alpha * progress),
                Color.Transparent,
                Color(0xFF00BCD4).copy(alpha = alpha * progress * 0.7f)
            )
        )
    )
}

private fun DrawScope.drawCosmicStoppedBackground(alpha: Float) {
    drawRect(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFFFF5722).copy(alpha = alpha),
                Color.Transparent,
                Color(0xFFE91E63).copy(alpha = alpha * 0.6f)
            ),
            radius = size.width * 0.8f
        )
    )
}

private fun DrawScope.drawCosmicCountingBackground(alpha: Float) {
    drawRect(
        brush = Brush.sweepGradient(
            colors = listOf(
                Color(0xFF4CAF50).copy(alpha = alpha),
                Color.Transparent,
                Color(0xFF8BC34A).copy(alpha = alpha * 0.7f),
                Color.Transparent
            )
        )
    )
}

// Data classes and enums
data class SpiralDrawingData(
    val automaticProgress: Float = 0f,
    val stoppedProgress: Float = 0f,
    val isDrawingComplete: Boolean = false
)

enum class SpiralStyle {
    AUTOMATIC
}