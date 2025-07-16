package io.github.warforged5.mashkmp.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import io.github.warforged5.mash.dataclasses.EliminationState
import io.github.warforged5.mashkmp.dataclasses.CategoryData

@Composable
fun NewEliminationCategoryCard(
    category: CategoryData,
    choices: List<String>,
    eliminationState: EliminationState,
    showFinalResults: Boolean
) {
    val categoryChoices = eliminationState.allChoices.filter { it.categoryName == category.realName }
    val hasActiveElimination = categoryChoices.any { choice ->
        eliminationState.currentCountingIndex >= 0 &&
                eliminationState.allChoices[eliminationState.currentCountingIndex].categoryName == category.realName
    }
    val finalChoice = categoryChoices.find { it.isFinal }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = if (hasActiveElimination) 8.dp else 4.dp,
        border = when {
            showFinalResults && finalChoice != null -> BorderStroke(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
            hasActiveElimination -> BorderStroke(2.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f))
            else -> null
        }
    ) {
        Box {
            // Glow effect for active category
            if (hasActiveElimination) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                                )
                            )
                        )
                )
            }

            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Category header
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = when {
                            showFinalResults && finalChoice != null -> MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                            hasActiveElimination -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                            else -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                category.icon,
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            category.nickname,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            when {
                                showFinalResults && finalChoice != null -> "✨ Winner: ${finalChoice.choiceText}"
                                hasActiveElimination -> "🎯 Counting now..."
                                categoryChoices.any { it.isEliminated } -> "⚡ Eliminating choices"
                                else -> "⏳ Awaiting turn"
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = when {
                                showFinalResults && finalChoice != null -> MaterialTheme.colorScheme.primary
                                hasActiveElimination -> MaterialTheme.colorScheme.secondary
                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }
                }

                // Choices
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    itemsIndexed(choices) { index, choice ->
                        val choiceState = categoryChoices.find {
                            it.choiceIndex == index && it.choiceText == choice
                        }
                        val isBeingCounted = eliminationState.currentCountingIndex >= 0 &&
                                eliminationState.allChoices[eliminationState.currentCountingIndex].let { current ->
                                    current.categoryName == category.realName && current.choiceIndex == index
                                }

                        NewChoiceChip(
                            choice = choice,
                            isEliminated = choiceState?.isEliminated == true,
                            isFinal = choiceState?.isFinal == true,
                            isBeingCounted = isBeingCounted
                        )
                    }
                }
            }
        }
    }
}
