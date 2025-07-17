package io.github.warforged5.mashkmp.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*

@Composable
fun ChoiceInputSection(
    choices: List<String>,
    categoryName: String,
    onAddChoice: (String) -> Unit,
    onRemoveChoice: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("") }
    var isInputFocused by remember { mutableStateOf(false) }
    var showValidationError by remember { mutableStateOf(false) }
    val focusManager = androidx.compose.ui.platform.LocalFocusManager.current
    val haptic = LocalHapticFeedback.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Enhanced input field
        EnhancedInputField(
            value = inputText,
            onValueChange = {
                inputText = it
                showValidationError = false
            },
            onFocusChange = { isInputFocused = it },
            categoryName = categoryName,
            isEnabled = choices.size < 6,
            showError = showValidationError,
            onSubmit = {
                if (inputText.isNotBlank() && choices.size < 6) {
                    if (!choices.contains(inputText.trim())) {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onAddChoice(inputText.trim())
                        inputText = ""
                        focusManager.clearFocus()
                    } else {
                        showValidationError = true
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    }
                }
            }
        )

        // Validation feedback with same styling as input
        AnimatedVisibility(
            visible = showValidationError,
            enter = slideInVertically(tween(300)) + fadeIn(tween(300)),
            exit = slideOutVertically(tween(200)) + fadeOut(tween(200))
        ) {
            ValidationErrorCard("This choice already exists!")
        }

        // Scrollable Choices list with consistent sizing
        AnimatedVisibility(
            visible = choices.isNotEmpty(),
            enter = fadeIn(tween(300)) + expandVertically(tween(300)),
            exit = fadeOut(tween(200)) + shrinkVertically(tween(200))
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header with choice count
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Your Choices",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                    ) {
                        Text(
                            "${choices.size}/6",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Scrollable choices container with max height
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp), // Limit height to make it scrollable
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                ) {
                    LazyColumn(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(vertical = 4.dp)
                    ) {
                        itemsIndexed(choices) { index, choice ->
                            ChoiceChip(
                                choice = choice,
                                index = index + 1,
                                onRemove = { onRemoveChoice(index) }
                            )
                        }
                    }
                }

                // Scroll hint when there are many choices
                if (choices.size >= 4) {
                    Text(
                        "💡 Scroll up to see all choices",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EnhancedInputField(
    value: String,
    onValueChange: (String) -> Unit,
    onFocusChange: (Boolean) -> Unit,
    categoryName: String,
    isEnabled: Boolean,
    showError: Boolean,
    onSubmit: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val haptic = LocalHapticFeedback.current

    LaunchedEffect(isFocused) {
        onFocusChange(isFocused)
    }

    val containerColor by animateColorAsState(
        targetValue = when {
            showError -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)
            isFocused -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
            !isEnabled -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        },
        animationSpec = tween(300, easing = FastOutSlowInEasing)
    )
    val borderColor by animateColorAsState(
        targetValue = when {
            showError -> MaterialTheme.colorScheme.error
            isFocused -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.surfaceVariant
        },
        animationSpec = tween(300, easing = FastOutSlowInEasing)
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = containerColor,
        border = BorderStroke(2.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                label = {
                    Text(
                        "Add choice for $categoryName",
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                placeholder = {
                    Text(
                        getPlaceholderForCategory(categoryName),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    )
                },
                modifier = Modifier.weight(1f),
                enabled = isEnabled,
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent,
                    errorBorderColor = Color.Transparent
                ),
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    imeAction = androidx.compose.ui.text.input.ImeAction.Done
                ),
                keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                    onDone = { onSubmit() }
                ),
                interactionSource = interactionSource
            )

            AnimatedVisibility(
                visible = value.isNotBlank() && isEnabled,
                enter = scaleIn(spring(dampingRatio = 0.5f)) + fadeIn(),
                exit = scaleOut(spring(dampingRatio = 0.5f)) + fadeOut()
            ) {
                FloatingActionButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onSubmit()
                    },
                    modifier = Modifier.size(48.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 6.dp,
                        pressedElevation = 2.dp
                    )
                ) {
                    Icon(
                        Icons.Rounded.Add,
                        contentDescription = "Add Choice",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun ValidationErrorCard(message: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.8f)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Rounded.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(20.dp)
            )
            Text(
                message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

private fun getPlaceholderForCategory(categoryName: String): String = when (categoryName.lowercase()) {
    "mash", "mash (housing)" -> "Villa, Cabin, Loft..."
    "spouse", "people" -> "Celebrity, Friend..."
    "number of kids", "number" -> "0, 2, 5..."
    "car", "vehicle" -> "Tesla, Bike, Truck..."
    "place to live", "places" -> "Paris, Tokyo, Beach..."
    "job", "occupations" -> "Artist, Doctor, Chef..."
    "color" -> "Purple, Gold, Navy..."
    "flavor" -> "Vanilla, Spicy, Sweet..."
    "pet" -> "Dog, Cat, Dragon..."
    "hobby" -> "Reading, Gaming, Hiking..."
    else -> "Enter your choice..."
}