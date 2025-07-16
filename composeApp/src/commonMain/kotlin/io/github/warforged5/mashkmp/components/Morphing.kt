package io.github.warforged5.mashkmp.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import kotlinx.coroutines.delay

@Composable
fun MorphingNavigationCard(
    title: String,
    description: String,
    icon: ImageVector,
    backgroundGradient: List<Color>,
    contentColor: Color,
    isSelected: Boolean,
    index: Int,
    onClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    var isPressed by remember { mutableStateOf(false) }
    var isHovered by remember { mutableStateOf(false) }

    // Complex morphing animations
    val morphingScale by animateFloatAsState(
        targetValue = when {
            isPressed -> 0.92f
            isSelected -> 1.05f
            isHovered -> 1.02f
            else -> 1f
        },
        animationSpec = spring(
            dampingRatio = 0.4f,
            stiffness = Spring.StiffnessMedium
        )
    )

    val morphingRotation by animateFloatAsState(
        targetValue = if (isSelected) 2f else 0f,
        animationSpec = spring(
            dampingRatio = 0.6f,
            stiffness = Spring.StiffnessLow
        )
    )

    val surfaceElevation by animateDpAsState(
        targetValue = when {
            isSelected -> 16.dp
            isHovered -> 8.dp
            else -> 4.dp
        },
        animationSpec = tween(300, easing = FastOutSlowInEasing)
    )

    val containerShape by animateFloatAsState(
        targetValue = if (isSelected) 32f else 20f,
        animationSpec = spring(
            dampingRatio = 0.7f,
            stiffness = Spring.StiffnessMedium
        )
    )

    // Entrance animation
    val entranceAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(
            durationMillis = 600,
            delayMillis = index * 150,
            easing = FastOutSlowInEasing
        )
    )

    val entranceSlide by animateFloatAsState(
        targetValue = 0f,
        animationSpec = tween(
            durationMillis = 800,
            delayMillis = index * 150,
            easing = FastOutSlowInEasing
        )
    )

    ElevatedCard(
        onClick = {
            isPressed = true
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            onClick()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .scale(morphingScale)
            .rotate(morphingRotation)
            .alpha(entranceAlpha)
            .offset(y = entranceSlide.dp),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = surfaceElevation,
            pressedElevation = 2.dp
        ),
        shape = RoundedCornerShape(containerShape.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = backgroundGradient,
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                )
        ) {
            // Morphing particle background
            MorphingParticleBackground(
                isActive = isSelected || isHovered,
                particleColor = Color.White.copy(alpha = 0.1f)
            )

            // Morphing content layout
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Morphing icon container
                MorphingIconContainer(
                    icon = icon,
                    contentColor = contentColor,
                    isSelected = isSelected,
                    isHovered = isHovered
                )

                // Content column with morphing text
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    MorphingText(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = contentColor,
                        fontWeight = FontWeight.Bold,
                        isSelected = isSelected
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    MorphingText(
                        text = description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = contentColor.copy(alpha = 0.9f),
                        isSelected = isSelected
                    )
                }

                // Morphing arrow indicator
                MorphingArrowIndicator(
                    contentColor = contentColor,
                    isSelected = isSelected
                )
            }
        }
    }

    // Reset pressed state
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(100)
            isPressed = false
        }
    }
}

@Composable
private fun MorphingIconContainer(
    icon: ImageVector,
    contentColor: Color,
    isSelected: Boolean,
    isHovered: Boolean
) {
    val iconSize by animateFloatAsState(
        targetValue = if (isSelected) 72f else 64f,
        animationSpec = spring(
            dampingRatio = 0.6f,
            stiffness = Spring.StiffnessMedium
        )
    )

    val iconRotation by animateFloatAsState(
        targetValue = if (isSelected) 360f else 0f,
        animationSpec = tween(1000, easing = FastOutSlowInEasing)
    )

    val containerScale by animateFloatAsState(
        targetValue = if (isHovered) 1.1f else 1f,
        animationSpec = spring(
            dampingRatio = 0.5f,
            stiffness = Spring.StiffnessMedium
        )
    )

    Surface(
        modifier = Modifier
            .size(iconSize.dp)
            .scale(containerScale)
            .rotate(iconRotation),
        shape = RoundedCornerShape(20.dp),
        color = Color.White.copy(alpha = if (isSelected) 0.3f else 0.2f)
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            // Pulsing background effect for selected state
            if (isSelected) {
                val infiniteTransition = rememberInfiniteTransition()
                val pulseAlpha by infiniteTransition.animateFloat(
                    initialValue = 0.3f,
                    targetValue = 0.6f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    )
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Color.White.copy(alpha = pulseAlpha),
                            RoundedCornerShape(20.dp)
                        )
                )
            }

            Icon(
                icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size((iconSize * 0.5f).dp)
            )
        }
    }
}

@Composable
private fun MorphingText(
    text: String,
    style: androidx.compose.ui.text.TextStyle,
    color: Color,
    fontWeight: FontWeight? = null,
    isSelected: Boolean
) {
    val textScale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = 0.7f,
            stiffness = Spring.StiffnessMedium
        )
    )

    val textColor by animateColorAsState(
        targetValue = if (isSelected) color else color.copy(alpha = 0.95f),
        animationSpec = tween(300)
    )

    Text(
        text = text,
        style = style,
        color = textColor,
        fontWeight = fontWeight,
        modifier = Modifier.scale(textScale)
    )
}

@Composable
private fun MorphingArrowIndicator(
    contentColor: Color,
    isSelected: Boolean
) {
    val arrowTranslation by animateFloatAsState(
        targetValue = if (isSelected) 8f else 0f,
        animationSpec = spring(
            dampingRatio = 0.6f,
            stiffness = Spring.StiffnessMedium
        )
    )

    val arrowAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.7f,
        animationSpec = tween(300)
    )

    Icon(
        Icons.Rounded.ArrowForward,
        contentDescription = null,
        tint = contentColor.copy(alpha = arrowAlpha),
        modifier = Modifier
            .size(24.dp)
            .offset(x = arrowTranslation.dp)
    )
}

@Composable
private fun MorphingParticleBackground(
    isActive: Boolean,
    particleColor: Color
) {
    if (!isActive) return

    val infiniteTransition = rememberInfiniteTransition()

    repeat(8) { index ->
        val animationDelay = index * 200

        val x by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 400f,
            animationSpec = infiniteRepeatable(
                animation = tween(3000 + animationDelay, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )

        val y by infiniteTransition.animateFloat(
            initialValue = index * 20f,
            targetValue = (index * 20f) + 40f,
            animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )

        val alpha by infiniteTransition.animateFloat(
            initialValue = 0.2f,
            targetValue = 0.6f,
            animationSpec = infiniteRepeatable(
                animation = tween(1500, delayMillis = animationDelay),
                repeatMode = RepeatMode.Reverse
            )
        )

        Canvas(
            modifier = Modifier
                .size(4.dp)
                .offset(x = x.dp, y = y.dp)
        ) {
            drawCircle(
                color = particleColor.copy(alpha = alpha),
                radius = size.width / 2
            )
        }
    }
}

@Composable
fun MorphingFloatingActionButton(
    onClick: () -> Unit,
    icon: ImageVector,
    text: String,
    isVisible: Boolean,
    backgroundGradient: List<Color> = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
    )
) {
    val haptic = LocalHapticFeedback.current
    var isPressed by remember { mutableStateOf(false) }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            animationSpec = spring(
                dampingRatio = 0.5f,
                stiffness = Spring.StiffnessMedium
            )
        ) { it } + fadeIn() + scaleIn(),
        exit = slideOutVertically() + fadeOut() + scaleOut()
    ) {
        val scale by animateFloatAsState(
            targetValue = if (isPressed) 0.9f else 1f,
            animationSpec = spring(
                dampingRatio = 0.4f,
                stiffness = Spring.StiffnessHigh
            )
        )

        val elevation by animateDpAsState(
            targetValue = if (isPressed) 2.dp else 12.dp,
            animationSpec = tween(150)
        )

        Surface(
            onClick = {
                isPressed = true
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick()
            },
            modifier = Modifier
                .scale(scale)
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            color = Color.Transparent,
            shadowElevation = elevation
        ) {
            Box(
                modifier = Modifier
                    .background(
                        Brush.linearGradient(
                            colors = backgroundGradient,
                            start = Offset(0f, 0f),
                            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                        )
                    )
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(100)
            isPressed = false
        }
    }
}

@Composable
fun MorphingProgressIndicator(
    progress: Float,
    currentStep: Int,
    totalSteps: Int,
    stepTitles: List<String>
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = spring(
            dampingRatio = 0.8f,
            stiffness = Spring.StiffnessLow
        )
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
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
                    "Progress",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                ) {
                    Text(
                        "$currentStep / $totalSteps",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Custom morphing progress bar
            MorphingProgressBar(
                progress = animatedProgress,
                currentStep = currentStep,
                totalSteps = totalSteps
            )

            // Current step indicator
            AnimatedContent(
                targetState = stepTitles.getOrNull(currentStep - 1) ?: "",
                transitionSpec = {
                    slideInVertically { it } + fadeIn() togetherWith
                            slideOutVertically { -it } + fadeOut()
                }
            ) { stepTitle ->
                Text(
                    stepTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun MorphingProgressBar(
    progress: Float,
    currentStep: Int,
    totalSteps: Int
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(12.dp)
    ) {
        val cornerRadius = size.height / 2
        val progressWidth = size.width * progress

        // Background track
        drawRoundRect(
            color = Color.Gray.copy(alpha = 0.2f),
            size = size.copy(height = size.height),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius)
        )

        // Progress fill with gradient
        if (progressWidth > 0) {
            drawRoundRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF6750A4),
                        Color(0xFF9C27B0),
                        Color(0xFF673AB7)
                    ),
                    startX = 0f,
                    endX = progressWidth
                ),
                size = size.copy(width = progressWidth),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius)
            )
        }

        // Step indicators
        repeat(totalSteps) { index ->
            val stepX = (size.width / (totalSteps - 1)) * index
            val isCompleted = index < currentStep
            val isCurrent = index == currentStep - 1

            drawCircle(
                color = when {
                    isCompleted -> Color(0xFF4CAF50)
                    isCurrent -> Color(0xFF6750A4)
                    else -> Color.Gray.copy(alpha = 0.5f)
                },
                radius = if (isCurrent) cornerRadius * 1.5f else cornerRadius,
                center = Offset(stepX, size.height / 2)
            )

            if (isCompleted) {
                // Draw checkmark for completed steps
                val checkSize = cornerRadius * 0.8f
                drawCircle(
                    color = Color.White,
                    radius = checkSize * 0.3f,
                    center = Offset(stepX, size.height / 2)
                )
            }
        }
    }
}