package io.github.warforged5.mashkmp.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import io.github.warforged5.mash.MashViewModel
import io.github.warforged5.mash.navigateToMashInput
import io.github.warforged5.mashkmp.components.*
import io.github.warforged5.mashkmp.dataclasses.*
import io.github.warforged5.mashkmp.enumclasses.*
import io.github.warforged5.mashkmp.components.SimplifiedTemplateCreationFlow
import io.github.warforged5.mashkmp.components.TemplateEditFlow
import io.github.warforged5.mashkmp.dataclasses.CategoryData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTemplateScreen(navController: NavController, viewModel: MashViewModel) {
    var showCreateFlow by remember { mutableStateOf(false) }
    var showEditFlow by remember { mutableStateOf(false) }
    var editingTemplate by remember { mutableStateOf<MashTemplate?>(null) }
    var showSuccessNotification by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }
    val haptic = LocalHapticFeedback.current

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Column {
                        Text("My Templates")
                        Text(
                            "${viewModel.templates.size} saved templates",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    showCreateFlow = true
                },
                icon = { Icon(Icons.Rounded.Add, contentDescription = null) },
                text = { Text("Create Template") },
                containerColor = MaterialTheme.colorScheme.primary
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Quick start section with enhanced cards
                if (viewModel.templates.isEmpty()) {
                    item {
                        EmptyTemplatesState(
                            onCreateTemplate = { showCreateFlow = true }
                        )
                    }
                } else {
                    // Template list
                    itemsIndexed(viewModel.templates) { index, template ->
                        EnhancedSavedTemplateCard(
                            template = template,
                            index = index,
                            onDelete = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.deleteTemplate(template)
                            },
                            onEdit = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                editingTemplate = template
                                showEditFlow = true
                            },
                            onUse = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                navController.navigateToMashInput(template.id)
                            }
                        )
                    }
                }
            }
        }
    }

    // Enhanced template creation flow
    if (showCreateFlow) {
        SimplifiedTemplateCreationFlow(
            onTemplateCreated = { template ->
                viewModel.saveTemplate(template)
                showCreateFlow = false
                successMessage = "✨ Template \"${template.name}\" created successfully!"
                showSuccessNotification = true
            },
            onDismiss = { showCreateFlow = false },
        )
    }

    // Template edit flow
    if (showEditFlow && editingTemplate != null) {
        TemplateEditFlow(
            existingTemplate = editingTemplate,
            onTemplateUpdated = { updatedTemplate ->
                viewModel.updateTemplate(updatedTemplate)
                showEditFlow = false
                editingTemplate = null
                successMessage = "✨ Template \"${updatedTemplate.name}\" updated successfully!"
                showSuccessNotification = true
            },
            onDismiss = {
                showEditFlow = false
                editingTemplate = null
            }
        )
    }

    // Success notification
    if (showSuccessNotification) {
        LaunchedEffect(successMessage) {
            kotlinx.coroutines.delay(3000)
            showSuccessNotification = false
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.primaryContainer,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Rounded.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    successMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
private fun EmptyTemplatesState(
    onCreateTemplate: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Animated illustration
        val infiniteTransition = rememberInfiniteTransition()
        val scale by infiniteTransition.animateFloat(
            initialValue = 0.9f,
            targetValue = 1.1f,
            animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )

        Surface(
            modifier = Modifier
                .size(120.dp)
                .scale(scale),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    "📝",
                    style = MaterialTheme.typography.displayLarge
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "No Templates Yet",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Create your first cosmic template to get started",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }

        Button(
            onClick = onCreateTemplate,
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Icon(Icons.Rounded.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Create First Template")
        }
    }
}

@Composable
private fun EnhancedSavedTemplateCard(
    template: MashTemplate,
    index: Int,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onUse: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }

    val animatedAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(
            durationMillis = 400,
            delayMillis = index * 50,
            easing = FastOutSlowInEasing
        )
    )

    Card(
        onClick = { isExpanded = !isExpanded },
        modifier = Modifier
            .fillMaxWidth()
            .alpha(animatedAlpha),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
        ),
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = when (template.type) {
                        MashType.CLASSIC -> MaterialTheme.colorScheme.primaryContainer
                        MashType.HYBRID -> MaterialTheme.colorScheme.secondaryContainer
                        MashType.CUSTOM -> MaterialTheme.colorScheme.tertiaryContainer
                    }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            when (template.type) {
                                MashType.CLASSIC -> Icons.Rounded.Home
                                MashType.HYBRID -> Icons.Rounded.Shuffle
                                MashType.CUSTOM -> Icons.Rounded.Build
                            },
                            contentDescription = null,
                            tint = when (template.type) {
                                MashType.CLASSIC -> MaterialTheme.colorScheme.onPrimaryContainer
                                MashType.HYBRID -> MaterialTheme.colorScheme.onSecondaryContainer
                                MashType.CUSTOM -> MaterialTheme.colorScheme.onTertiaryContainer
                            },
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        template.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "${template.categories.size} categories • ${template.type.name.lowercase()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row {
                    IconButton(
                        onClick = { showDeleteDialog = true }
                    ) {
                        Icon(
                            Icons.Rounded.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                        )
                    }

                    IconButton(
                        onClick = onEdit
                    ) {
                        Icon(
                            Icons.Rounded.Edit,
                            contentDescription = "Edit",
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                        )
                    }

                    FilledTonalIconButton(
                        onClick = onUse
                    ) {
                        Icon(
                            Icons.Rounded.PlayArrow,
                            contentDescription = "Use"
                        )
                    }
                }
            }

            // Expandable content
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp).fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "Categories",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(template.categories) { category ->
                                CompactCategoryChip(category)
                            }
                        }

                        // Additional actions in expanded view
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = onEdit,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Rounded.Edit, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Edit")
                            }

                            Button(
                                onClick = onUse,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Rounded.PlayArrow, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Play Now")
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            icon = {
                Icon(Icons.Rounded.Delete, contentDescription = null)
            },
            title = { Text("Delete Template") },
            text = { Text("Delete \"${template.name}\"? This cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun CompactCategoryChip(category: CategoryData) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                category.icon,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                category.nickname,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}