package io.github.warforged5.mashkmp.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import io.github.warforged5.mashkmp.dataclasses.EliminationChoice
import io.github.warforged5.mashkmp.dataclasses.EliminationState
import io.github.warforged5.mashkmp.dataclasses.CategoryData
import kotlinx.coroutines.delay

@Composable
fun NewEliminationSystem(
    allChoices: Map<CategoryData, List<String>>,
    spiralCount: Int,
    onComplete: (Map<String, String>) -> Unit,
    onGenerateStory: ((Map<String, String>) -> Unit)? = null
) {
    var eliminationState by remember {
        mutableStateOf(
            EliminationState(
                allChoices = buildInitialChoicesList(allChoices)
            )
        )
    }

    var isAnimating by remember { mutableStateOf(false) }
    var showFinalResults by remember { mutableStateOf(false) }
    var showActionButtons by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableStateOf(0) } // Track position across eliminations
    val haptic = LocalHapticFeedback.current

    // Start elimination process
    LaunchedEffect(Unit) {
        delay(1000) // Initial pause

        while (!eliminationState.eliminationsComplete) {
            // Get active choices (not eliminated, categories with >1 choice)
            val activeChoices = getActiveChoices(eliminationState.allChoices)

            if (activeChoices.isEmpty()) break

            // Start counting animation
            eliminationState = eliminationState.copy(isCountingActive = true)
            isAnimating = true

            // Animate through counting positions, starting from current position
            var countPosition = currentPosition % activeChoices.size
            repeat(spiralCount) {
                eliminationState = eliminationState.copy(
                    currentCountingIndex = activeChoices[countPosition % activeChoices.size].let { choice ->
                        eliminationState.allChoices.indexOfFirst {
                            it.categoryName == choice.categoryName &&
                                    it.choiceIndex == choice.choiceIndex
                        }
                    }
                )
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                delay(250) // Counting speed
                countPosition++
            }

            // Eliminate the choice we landed on
            val finalPosition = (countPosition - 1) % activeChoices.size
            val eliminationChoice = activeChoices[finalPosition]

            // Update current position to continue from next choice
            currentPosition = finalPosition

            eliminationState = eliminationState.copy(
                allChoices = eliminationState.allChoices.map { choice ->
                    if (choice.categoryName == eliminationChoice.categoryName &&
                        choice.choiceIndex == eliminationChoice.choiceIndex) {
                        choice.copy(isEliminated = true)
                    } else choice
                },
                isCountingActive = false,
                currentCountingIndex = -1,
                currentRound = eliminationState.currentRound + 1
            )

            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            delay(800) // Pause after elimination

            // Check if we're done
            val finalChoices = getFinalChoices(eliminationState.allChoices, allChoices.keys.toList())
            if (finalChoices.isNotEmpty() && finalChoices.size == allChoices.keys.size) {
                // Mark final choices
                eliminationState = eliminationState.copy(
                    allChoices = eliminationState.allChoices.map { choice ->
                        val isWinner = finalChoices[choice.categoryName] == choice.choiceText
                        choice.copy(isFinal = isWinner)
                    },
                    eliminationsComplete = true
                )
                break
            }
        }

        isAnimating = false
        delay(1000)
        showFinalResults = true
        delay(1000)
        showActionButtons = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.05f)
                    )
                )
            )
            .statusBarsPadding() // This adds padding for the status bar/dynamic island
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Header
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(tween(1000)) + slideInVertically(tween(1000)) { -it }
        ) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                shadowElevation = 0.dp
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 32.dp, vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "Spiral Elimination ($spiralCount)",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        when {
                            showFinalResults -> "✨ Your Destiny is Revealed ✨"
                            eliminationState.isCountingActive -> {
                                val currentChoice = eliminationState.allChoices.getOrNull(eliminationState.currentCountingIndex)
                                "Counting in ${currentChoice?.categoryName ?: "..."}"
                            }
                            isAnimating -> "Round ${eliminationState.currentRound + 1} - Eliminating..."
                            else -> "Preparing elimination..."
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Categories
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(allChoices.keys.toList()) { category ->
                NewEliminationCategoryCard(
                    category = category,
                    choices = allChoices[category] ?: emptyList(),
                    eliminationState = eliminationState,
                    showFinalResults = showFinalResults
                )
            }
        }

        // Action buttons
        AnimatedVisibility(
            visible = showActionButtons,
            enter = fadeIn(tween(800)) + slideInVertically(tween(800)) { it / 2 }
        ) {
            val finalResults = getFinalChoices(eliminationState.allChoices, allChoices.keys.toList())

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (onGenerateStory != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onComplete(finalResults) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Rounded.PlayArrow, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("See Results")
                        }

                        Button(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onGenerateStory(finalResults)
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Rounded.AutoAwesome, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("AI Story")
                        }
                    }
                } else {
                    Button(
                        onClick = { onComplete(finalResults) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Rounded.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("See Your Destiny", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}


// Helper functions
private fun buildInitialChoicesList(allChoices: Map<CategoryData, List<String>>): List<EliminationChoice> {
    val choices = mutableListOf<EliminationChoice>()
    allChoices.forEach { (category, categoryChoices) ->
        categoryChoices.forEachIndexed { index, choice ->
            choices.add(
                EliminationChoice(
                    categoryName = category.realName,
                    choiceIndex = index,
                    choiceText = choice
                )
            )
        }
    }
    return choices
}

private fun getActiveChoices(allChoices: List<EliminationChoice>): List<EliminationChoice> {
    // Group by category and only include categories that have more than 1 non-eliminated choice
    val choicesByCategory = allChoices.groupBy { it.categoryName }
    val activeChoices = mutableListOf<EliminationChoice>()

    choicesByCategory.forEach { (_, categoryChoices) ->
        val nonEliminated = categoryChoices.filter { !it.isEliminated }
        if (nonEliminated.size > 1) {
            activeChoices.addAll(nonEliminated)
        }
    }

    return activeChoices
}

private fun getFinalChoices(allChoices: List<EliminationChoice>, categories: List<CategoryData>): Map<String, String> {
    val finalChoices = mutableMapOf<String, String>()

    categories.forEach { category ->
        val categoryChoices = allChoices.filter { it.categoryName == category.realName }
        val nonEliminated = categoryChoices.filter { !it.isEliminated }
        if (nonEliminated.size == 1) {
            finalChoices[category.realName] = nonEliminated.first().choiceText
        }
    }

    return finalChoices
}