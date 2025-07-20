package io.github.warforged5.mashkmp.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Dialog
import io.github.warforged5.mashkmp.dataclasses.CategoryData
import io.github.warforged5.mashkmp.dataclasses.MashTemplate
import io.github.warforged5.mashkmp.enumclasses.MashType
import kotlinx.coroutines.delay
import kotlin.math.pow

@Composable
fun SimplifiedTemplateCreationFlow(
    onTemplateCreated: (MashTemplate) -> Unit,
    onDismiss: () -> Unit
) {
    var currentStep by remember { mutableStateOf(TemplateStep.NAME) }
    var templateName by remember { mutableStateOf("") }
    var categories by remember { mutableStateOf(mutableListOf<CategoryData>()) }
    var nameError by remember { mutableStateOf<String?>(null) }
    val haptic = LocalHapticFeedback.current

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 16.dp
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header with progress
                TemplateCreationHeader(
                    currentStep = currentStep,
                    templateName = templateName,
                    categoryCount = categories.size,
                    onDismiss = onDismiss
                )

                // Step content
                AnimatedContent(
                    targetState = currentStep,
                    transitionSpec = {
                        slideInHorizontally(
                            animationSpec = tween(300, easing = FastOutSlowInEasing)
                        ) { if (targetState.ordinal > initialState.ordinal) it else -it } +
                                fadeIn(tween(300)) togetherWith
                                slideOutHorizontally(
                                    animationSpec = tween(200)
                                ) { if (targetState.ordinal > initialState.ordinal) -it else it } +
                                fadeOut(tween(200))
                    },
                    modifier = Modifier.weight(1f)
                ) { step ->
                    when (step) {
                        TemplateStep.NAME -> {
                            NameStep(
                                templateName = templateName,
                                onNameChange = {
                                    templateName = it
                                    nameError = null
                                },
                                nameError = nameError
                            )
                        }

                        TemplateStep.CATEGORIES -> {
                            CategoriesStep(
                                categories = categories,
                                onCategoriesChange = { categories = it }
                            )
                        }

                        TemplateStep.PREVIEW -> {
                            PreviewStep(
                                templateName = templateName,
                                categories = categories
                            )
                        }
                    }
                }

                // Vertical navigation controls
                VerticalNavigationControls(
                    currentStep = currentStep,
                    canProceed = when (currentStep) {
                        TemplateStep.NAME -> templateName.isNotBlank()
                        TemplateStep.CATEGORIES -> categories.size >= 3
                        TemplateStep.PREVIEW -> true
                    },
                    onPrevious = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        currentStep = TemplateStep.values()[currentStep.ordinal - 1]
                    },
                    onNext = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        when (currentStep) {
                            TemplateStep.NAME -> {
                                if (templateName.isBlank()) {
                                    nameError = "Template name is required"
                                } else {
                                    currentStep = TemplateStep.CATEGORIES
                                }
                            }
                            TemplateStep.CATEGORIES -> {
                                currentStep = TemplateStep.PREVIEW
                            }
                            TemplateStep.PREVIEW -> {
                                val template = MashTemplate(
                                    name = templateName,
                                    categories = categories,
                                    type = MashType.CUSTOM // Always custom since users create their own
                                )
                                onTemplateCreated(template)
                            }
                        }
                    },
                    onCancel = onDismiss
                )
            }
        }
    }
}

@Composable
private fun TemplateCreationHeader(
    currentStep: TemplateStep,
    templateName: String,
    categoryCount: Int,
    onDismiss: () -> Unit
) {
    Surface(
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
                Column {
                    Text(
                        "Create Template",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        when (currentStep) {
                            TemplateStep.NAME -> "Choose a name for your template"
                            TemplateStep.CATEGORIES -> "Add your categories"
                            TemplateStep.PREVIEW -> "Review and save"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Rounded.Close, contentDescription = "Close")
                }
            }

            // Simple progress indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TemplateStep.values().forEachIndexed { index, step ->
                    val isCompleted = index < currentStep.ordinal
                    val isCurrent = index == currentStep.ordinal

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp),
                        shape = RoundedCornerShape(2.dp),
                        color = when {
                            isCompleted -> MaterialTheme.colorScheme.primary
                            isCurrent -> MaterialTheme.colorScheme.secondary
                            else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        }
                    ) {}
                }
            }

            // Quick status
            if (templateName.isNotBlank() || categoryCount > 0) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (templateName.isNotBlank()) {
                        StatusChip(
                            text = "\"$templateName\"",
                            icon = Icons.Rounded.Edit
                        )
                    }
                    if (categoryCount > 0) {
                        StatusChip(
                            text = "$categoryCount categories",
                            icon = Icons.Rounded.Category
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun StatusChip(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = MaterialTheme.colorScheme.secondary
            )
            Text(
                text,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun NameStep(
    templateName: String,
    onNameChange: (String) -> Unit,
    nameError: String?
) {
    LazyColumn(
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Name Your Template",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        item {
            NameInputSection(
                value = templateName,
                onValueChange = onNameChange,
                error = nameError
            )
        }
    }
}

@Composable
private fun NameInputSection(
    value: String,
    onValueChange: (String) -> Unit,
    error: String?
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        delay(300) // Wait for dialog animation
        focusRequester.requestFocus()
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text("Template Name") },
            placeholder = { Text("My Awesome Template") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            isError = error != null,
            supportingText = error?.let { {
                Text(it, color = MaterialTheme.colorScheme.error)
            } },
            trailingIcon = {
                if (value.isNotBlank()) {
                    IconButton(onClick = { onValueChange("") }) {
                        Icon(Icons.Rounded.Clear, contentDescription = "Clear")
                    }
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        // Character count
        Text(
            "${value.length}/50 characters",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.align(Alignment.End)
        )

        // Quick suggestions
        if (value.isBlank()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(templateNameSuggestions) { suggestion ->
                    SuggestionChip(
                        onClick = { onValueChange(suggestion) },
                        label = { Text(suggestion) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ExamplesSection() {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "💡 Examples",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            val examples = listOf(
                "Future Career Path",
                "Dream Vacation",
                "College Life",
                "Superhero Identity",
                "Fantasy Adventure"
            )

            examples.forEach { example ->
                Text(
                    "• $example",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun CategoriesStep(
    categories: MutableList<CategoryData>,
    onCategoriesChange: (MutableList<CategoryData>) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    LazyColumn(
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Add Categories",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        item {
            CategoryStatusCard(
                currentCount = categories.size,
                minimumRequired = 3
            )
        }

        item {
            Button(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    showAddDialog = true
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Rounded.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add New Category")
            }
        }

        // Categories list
        if (categories.isNotEmpty()) {
            item {
                Text(
                    "Your Categories (${categories.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            itemsIndexed(categories) { index, category ->
                CategoryListItem(
                    category = category,
                    index = index,
                    onEdit = { newCategory ->
                        val newCategories = categories.toMutableList()
                        newCategories[index] = newCategory
                        onCategoriesChange(newCategories)
                    },
                    onDelete = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onCategoriesChange(categories.toMutableList().apply { removeAt(index) })
                    },
                    onMoveUp = if (index > 0) {
                        {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            val newCategories = categories.toMutableList()
                            val temp = newCategories[index]
                            newCategories[index] = newCategories[index - 1]
                            newCategories[index - 1] = temp
                            onCategoriesChange(newCategories)
                        }
                    } else null,
                    onMoveDown = if (index < categories.size - 1) {
                        {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            val newCategories = categories.toMutableList()
                            val temp = newCategories[index]
                            newCategories[index] = newCategories[index + 1]
                            newCategories[index + 1] = temp
                            onCategoriesChange(newCategories)
                        }
                    } else null
                )
            }
        } else {
            item {
                EmptyCategoriesState(
                    onAddCategory = { showAddDialog = true }
                )
            }
        }
    }

    if (showAddDialog) {
        AddCategoryDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { categoryData: CategoryData ->
                onCategoriesChange((categories + categoryData).toMutableList())
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun CategoryStatusCard(
    currentCount: Int,
    minimumRequired: Int
) {
    val isMinimumMet = currentCount >= minimumRequired

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isMinimumMet)
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        else
            MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                if (isMinimumMet) Icons.Rounded.CheckCircle else Icons.Rounded.Warning,
                contentDescription = null,
                tint = if (isMinimumMet) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    if (isMinimumMet) "Ready to continue!" else "Need more categories",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isMinimumMet) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
                Text(
                    if (isMinimumMet) {
                        "You have $currentCount categories. Perfect for a great game!"
                    } else {
                        "Add ${minimumRequired - currentCount} more categories to continue."
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            Surface(
                shape = CircleShape,
                color = if (isMinimumMet)
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                else
                    MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
            ) {
                Text(
                    currentCount.toString(),
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (isMinimumMet) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun CategoryListItem(
    category: CategoryData,
    index: Int,
    onEdit: (CategoryData) -> Unit,
    onDelete: () -> Unit,
    onMoveUp: (() -> Unit)?,
    onMoveDown: (() -> Unit)?
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }

    ElevatedCard(
        onClick = { isExpanded = !isExpanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(40.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            category.icon,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        category.realName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (category.nickname != category.realName) {
                        Text(
                            "Shows as: ${category.nickname}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Icon(
                    if (isExpanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                    contentDescription = if (isExpanded) "Collapse" else "Expand"
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (onMoveUp != null) {
                            FilledTonalIconButton(
                                onClick = onMoveUp,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    Icons.Rounded.KeyboardArrowUp,
                                    contentDescription = "Move Up",
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        if (onMoveDown != null) {
                            FilledTonalIconButton(
                                onClick = onMoveDown,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    Icons.Rounded.KeyboardArrowDown,
                                    contentDescription = "Move Down",
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        FilledTonalIconButton(
                            onClick = { showEditDialog = true },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                Icons.Rounded.Edit,
                                contentDescription = "Edit",
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        FilledTonalIconButton(
                            onClick = onDelete,
                            modifier = Modifier.size(36.dp),
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                            )
                        ) {
                            Icon(
                                Icons.Rounded.Delete,
                                contentDescription = "Remove",
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    if (showEditDialog) {
        EditCategoryDialog(
            currentCategory = category,
            onDismiss = { showEditDialog = false },
            onSave = { newCategory ->
                onEdit(newCategory)
                showEditDialog = false
            }
        )
    }
}

@Composable
private fun EmptyCategoriesState(
    onAddCategory: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "📝",
                style = MaterialTheme.typography.displayMedium
            )

            Text(
                "No categories yet",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                "Add your first category to get started",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Button(onClick = onAddCategory) {
                Icon(Icons.Rounded.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Category")
            }
        }
    }
}

@Composable
private fun PreviewStep(
    templateName: String,
    categories: List<CategoryData>
) {
    LazyColumn(
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Template Complete!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        item {
            TemplatePreviewCard(
                templateName = templateName,
                categories = categories
            )
        }

        item {
            TemplateStatsCard(categories.size)
        }
    }
}

@Composable
private fun TemplatePreviewCard(
    templateName: String,
    categories: List<CategoryData>
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Rounded.AutoAwesome,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Column {
                    Text(
                        templateName,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Custom Template",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Text(
                "Categories (${categories.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                category.icon,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                category.nickname,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TemplateStatsCard(categoryCount: Int) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier.padding(20.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(
                icon = Icons.Rounded.Category,
                value = categoryCount.toString(),
                label = "Categories"
            )

            StatItem(
                icon = Icons.Rounded.Timeline,
                value = "${4.0.pow(categoryCount.toDouble()).toInt()}+",
                label = "Possibilities"
            )

            StatItem(
                icon = Icons.Rounded.AutoAwesome,
                value = "∞",
                label = "Fun Level"
            )
        }
    }
}

@Composable
internal fun StatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Text(
            value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun VerticalNavigationControls(
    currentStep: TemplateStep,
    canProceed: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onCancel: () -> Unit
) {
    Surface(
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Main action button
            Button(
                onClick = onNext,
                enabled = canProceed,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    when (currentStep) {
                        TemplateStep.NAME -> "Add Categories"
                        TemplateStep.CATEGORIES -> "Preview Template"
                        TemplateStep.PREVIEW -> "Create Template"
                    },
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    if (currentStep == TemplateStep.PREVIEW)
                        Icons.Rounded.Save
                    else
                        Icons.Rounded.ArrowForward,
                    contentDescription = null
                )
            }

            // Secondary actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Rounded.Close, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Cancel")
                }

                if (currentStep != TemplateStep.NAME) {
                    OutlinedButton(
                        onClick = onPrevious,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Previous")
                    }
                }
            }
        }
    }
}

@Composable
internal fun AddCategoryDialog(
    onDismiss: () -> Unit,
    onAdd: (CategoryData) -> Unit
) {
    var realName by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var icon by remember { mutableStateOf("⭐") }
    var showIconPicker by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    if (showIconPicker) {
        IconSelectionSheet(
            onDismiss = { showIconPicker = false },
            onIconSelected = {
                icon = it
                showIconPicker = false
            }
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    "Add New Category",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = realName,
                        onValueChange = {
                            realName = it
                            if (nickname.isEmpty()) {
                                nickname = it // Auto-fill nickname
                            }
                        },
                        label = { Text("Category Name") },
                        placeholder = { Text("e.g., Future Job, Dream Car") },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequester),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) }
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clickable { showIconPicker = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(icon, style = MaterialTheme.typography.headlineMedium)
                    }
                }


                OutlinedTextField(
                    value = nickname,
                    onValueChange = { nickname = it },
                    label = { Text("Display Name (optional)") },
                    placeholder = { Text("Short name shown to players") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (realName.isNotBlank()) {
                                onAdd(
                                    CategoryData(
                                        realName = realName,
                                        nickname = nickname.ifBlank { realName },
                                        icon = icon,
                                        isClassic = false
                                    )
                                )
                            }
                        }
                    )
                )

                Text(
                    "💡 Examples: Future Job, Dream House, Pet, College, Vacation Destination",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    FilledTonalButton(
                        onClick = {
                            onAdd(
                                CategoryData(
                                    realName = realName,
                                    nickname = nickname.ifBlank { realName },
                                    icon = icon,
                                    isClassic = false
                                )
                            )
                        },
                        enabled = realName.isNotBlank()
                    ) {
                        Text("Add")
                    }
                }
            }
        }
    }
}

@Composable
internal fun EditCategoryDialog(
    currentCategory: CategoryData,
    onDismiss: () -> Unit,
    onSave: (CategoryData) -> Unit
) {
    var realName by remember { mutableStateOf(currentCategory.realName) }
    var nickname by remember { mutableStateOf(currentCategory.nickname) }
    var icon by remember { mutableStateOf(currentCategory.icon) }
    var showIconPicker by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    if (showIconPicker) {
        IconSelectionSheet(
            onDismiss = { showIconPicker = false },
            onIconSelected = {
                icon = it
                showIconPicker = false
            }
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    "Edit Category",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = realName,
                        onValueChange = { realName = it },
                        label = { Text("Category Name") },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequester),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clickable { showIconPicker = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(icon, style = MaterialTheme.typography.headlineMedium)
                    }
                }

                OutlinedTextField(
                    value = nickname,
                    onValueChange = { nickname = it },
                    label = { Text("Display Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            onSave(
                                CategoryData(
                                    realName = realName,
                                    nickname = nickname.ifBlank { realName },
                                    icon = icon,
                                    isClassic = currentCategory.isClassic
                                )
                            )
                        },
                        enabled = realName.isNotBlank()
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}



// Data classes
enum class TemplateStep {
    NAME, CATEGORIES, PREVIEW
}

private val templateNameSuggestions = listOf(
    "Future Life",
    "Dream Path",
    "My Destiny",
    "Life Adventure",
    "Perfect World"
)