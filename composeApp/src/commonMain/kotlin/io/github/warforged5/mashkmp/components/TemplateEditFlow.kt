package io.github.warforged5.mashkmp.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Dialog
import io.github.warforged5.mashkmp.dataclasses.CategoryData
import io.github.warforged5.mashkmp.dataclasses.MashTemplate
import io.github.warforged5.mashkmp.enumclasses.MashType
import kotlinx.coroutines.delay
import kotlin.math.pow

@Composable
fun TemplateEditFlow(
    existingTemplate: MashTemplate? = null,
    onTemplateUpdated: (MashTemplate) -> Unit,
    onDismiss: () -> Unit
) {
    var currentStep by remember { mutableStateOf(TemplateStep.NAME) }
    var templateName by remember { mutableStateOf(existingTemplate?.name ?: "") }
    var categories by remember {
        mutableStateOf(mutableListOf<CategoryData>().apply {
            existingTemplate?.categories?.filterNotNull()?.forEach { category ->
                // Ensure all category fields are non-null
                if (category.realName.isNotBlank()) {
                    add(
                        CategoryData(
                        realName = category.realName,
                        nickname = category.nickname.takeIf { it.isNotBlank() } ?: category.realName,
                        icon = category.icon.takeIf { it.isNotBlank() } ?: "⭐",
                        isClassic = category.isClassic
                    ))
                }
            }
        })
    }
    var nameError by remember { mutableStateOf<String?>(null) }
    val haptic = LocalHapticFeedback.current
    val isEditing = existingTemplate != null

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
                TemplateEditHeader(
                    currentStep = currentStep,
                    templateName = templateName,
                    categoryCount = categories.size,
                    isEditing = isEditing,
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
                            NameEditStep(
                                templateName = templateName,
                                onNameChange = {
                                    templateName = it
                                    nameError = null
                                },
                                nameError = nameError,
                                isEditing = isEditing
                            )
                        }

                        TemplateStep.CATEGORIES -> {
                            CategoriesEditStep(
                                categories = categories,
                                onCategoriesChange = { categories = it },
                                isEditing = isEditing
                            )
                        }

                        TemplateStep.PREVIEW -> {
                            PreviewEditStep(
                                templateName = templateName,
                                categories = categories,
                                isEditing = isEditing
                            )
                        }
                    }
                }

                // Vertical navigation controls
                VerticalEditNavigationControls(
                    currentStep = currentStep,
                    canProceed = when (currentStep) {
                        TemplateStep.NAME -> templateName.isNotBlank()
                        TemplateStep.CATEGORIES -> categories.size >= 3
                        TemplateStep.PREVIEW -> true
                    },
                    isEditing = isEditing,
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
                                val template = if (isEditing && existingTemplate != null) {
                                    existingTemplate.copy(
                                        name = templateName,
                                        categories = categories
                                    )
                                } else {
                                    MashTemplate(
                                        name = templateName,
                                        categories = categories,
                                        type = MashType.CUSTOM
                                    )
                                }
                                onTemplateUpdated(template)
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
private fun TemplateEditHeader(
    currentStep: TemplateStep,
    templateName: String,
    categoryCount: Int,
    isEditing: Boolean,
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
                        if (isEditing) "Edit Template" else "Create Template",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        when (currentStep) {
                            TemplateStep.NAME -> if (isEditing) "Update the template name" else "Choose a name for your template"
                            TemplateStep.CATEGORIES -> if (isEditing) "Modify your categories" else "Add your categories"
                            TemplateStep.PREVIEW -> "Review and save changes"
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
private fun NameEditStep(
    templateName: String,
    onNameChange: (String) -> Unit,
    nameError: String?,
    isEditing: Boolean
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
                    if (isEditing) "Update Template Name" else "Name Your Template",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        item {
            NameEditInputSection(
                value = templateName,
                onValueChange = onNameChange,
                error = nameError,
                isEditing = isEditing
            )
        }
    }
}

@Composable
private fun NameEditInputSection(
    value: String,
    onValueChange: (String) -> Unit,
    error: String?,
    isEditing: Boolean
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        if (!isEditing) {
            delay(300) // Wait for dialog animation
            focusRequester.requestFocus()
        }
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
    }
}

@Composable
private fun CategoriesEditStep(
    categories: MutableList<CategoryData>,
    onCategoriesChange: (MutableList<CategoryData>) -> Unit,
    isEditing: Boolean
) {
    var showAddDialog by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    // Ensure categories list is never null and contains valid data
    val safeCategories = remember(categories) {
        categories.filterNotNull().filter {
            it.realName.isNotBlank()
        }.map { category ->
            CategoryData(
                realName = category.realName,
                nickname = category.nickname.takeIf { it.isNotBlank() } ?: category.realName,
                icon = category.icon.takeIf { it.isNotBlank() } ?: "⭐",
                isClassic = category.isClassic
            )
        }.toMutableList()
    }

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
                    if (isEditing) "Modify Categories" else "Add Categories",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        item {
            CategoryEditStatusCard(
                currentCount = safeCategories.size,
                minimumRequired = 3,
                isEditing = isEditing
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
        if (safeCategories.isNotEmpty()) {
            item {
                Text(
                    "Your Categories (${safeCategories.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            itemsIndexed(safeCategories) { index, category ->
                CategoryEditItem(
                    category = category,
                    index = index,
                    onEdit = { newCategory ->
                        try {
                            val newCategories = safeCategories.toMutableList()
                            if (index >= 0 && index < newCategories.size) {
                                newCategories[index] = newCategory
                                onCategoriesChange(newCategories)
                            }
                        } catch (e: Exception) {
                            // Handle any index out of bounds or other exceptions
                            val newCategories = safeCategories.filterIndexed { i, _ -> i != index }.toMutableList()
                            newCategories.add(newCategory)
                            onCategoriesChange(newCategories)
                        }
                    },
                    onDelete = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        try {
                            val newCategories = safeCategories.toMutableList()
                            if (index >= 0 && index < newCategories.size) {
                                newCategories.removeAt(index)
                                onCategoriesChange(newCategories)
                            }
                        } catch (e: Exception) {
                            // Handle any index out of bounds exceptions
                            val newCategories = safeCategories.filterIndexed { i, _ -> i != index }.toMutableList()
                            onCategoriesChange(newCategories)
                        }
                    },
                    onMoveUp = if (index > 0) {
                        {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            try {
                                val newCategories = safeCategories.toMutableList()
                                if (index > 0 && index < newCategories.size) {
                                    val temp = newCategories[index]
                                    newCategories[index] = newCategories[index - 1]
                                    newCategories[index - 1] = temp
                                    onCategoriesChange(newCategories)
                                }
                            } catch (e: Exception) {
                                // If something goes wrong, just keep the original list
                            }
                        }
                    } else null,
                    onMoveDown = if (index < safeCategories.size - 1) {
                        {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            try {
                                val newCategories = safeCategories.toMutableList()
                                if (index >= 0 && index < newCategories.size - 1) {
                                    val temp = newCategories[index]
                                    newCategories[index] = newCategories[index + 1]
                                    newCategories[index + 1] = temp
                                    onCategoriesChange(newCategories)
                                }
                            } catch (e: Exception) {
                                // If something goes wrong, just keep the original list
                            }
                        }
                    } else null
                )
            }
        }
    }

    if (showAddDialog) {
        AddCategoryDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { categoryData: CategoryData ->
                try {
                    val newCategories = safeCategories.toMutableList()
                    newCategories.add(categoryData)
                    onCategoriesChange(newCategories)
                    showAddDialog = false
                } catch (e: Exception) {
                    showAddDialog = false
                }
            }
        )
    }
}

@Composable
private fun CategoryEditStatusCard(
    currentCount: Int,
    minimumRequired: Int,
    isEditing: Boolean
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
                        "You have $currentCount categories. ${if (isEditing) "Perfect setup!" else "Perfect for a great game!"}"
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
private fun CategoryEditItem(
    category: CategoryData,
    index: Int,
    onEdit: (CategoryData) -> Unit,
    onDelete: () -> Unit,
    onMoveUp: (() -> Unit)?,
    onMoveDown: (() -> Unit)?
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }

    // Ensure category has valid data
    val safeCategory = remember(category) {
        CategoryData(
            realName = category.realName.takeIf { it.isNotBlank() } ?: "Unnamed Category",
            nickname = category.nickname.takeIf { it.isNotBlank() } ?: category.realName.takeIf { it.isNotBlank() } ?: "Unnamed",
            icon = category.icon.takeIf { it.isNotBlank() } ?: "⭐",
            isClassic = category.isClassic
        )
    }

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
                            safeCategory.icon,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        safeCategory.realName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (safeCategory.nickname != safeCategory.realName) {
                        Text(
                            "Shows as: ${safeCategory.nickname}",
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
            currentCategory = safeCategory,
            onDismiss = { showEditDialog = false },
            onSave = { newCategory ->
                onEdit(newCategory)
                showEditDialog = false
            }
        )
    }
}

@Composable
private fun PreviewEditStep(
    templateName: String,
    categories: List<CategoryData>,
    isEditing: Boolean
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
                    if (isEditing) "Review Changes" else "Template Complete!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        item {
            TemplateEditPreviewCard(
                templateName = templateName,
                categories = categories,
                isEditing = isEditing
            )
        }

        item {
            TemplateEditStatsCard(categories.size, isEditing)
        }
    }
}

@Composable
private fun TemplateEditPreviewCard(
    templateName: String,
    categories: List<CategoryData>,
    isEditing: Boolean
) {
    // Ensure template name and categories are safe
    val safeName = templateName.takeIf { it.isNotBlank() } ?: "Unnamed Template"
    val safeCategories = categories.filterNotNull().filter { it.realName.isNotBlank() }

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
                            if (isEditing) Icons.Rounded.Edit else Icons.Rounded.AutoAwesome,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Column {
                    Text(
                        safeName,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        if (isEditing) "Updated Template" else "Custom Template",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Text(
                "Categories (${safeCategories.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            if (safeCategories.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(safeCategories) { category ->
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
                                    category.icon.takeIf { it.isNotBlank() } ?: "⭐",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    category.nickname.takeIf { it.isNotBlank() } ?: category.realName,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                    }
                }
            } else {
                Text(
                    "No categories added yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
private fun TemplateEditStatsCard(categoryCount: Int, isEditing: Boolean) {
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
                icon = if (isEditing) Icons.Rounded.Edit else Icons.Rounded.AutoAwesome,
                value = if (isEditing) "✓" else "∞",
                label = if (isEditing) "Updated" else "Fun Level"
            )
        }
    }
}

@Composable
private fun VerticalEditNavigationControls(
    currentStep: TemplateStep,
    canProceed: Boolean,
    isEditing: Boolean,
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
                        TemplateStep.NAME -> if (isEditing) "Update Categories" else "Add Categories"
                        TemplateStep.CATEGORIES -> "Preview Template"
                        TemplateStep.PREVIEW -> if (isEditing) "Save Changes" else "Create Template"
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