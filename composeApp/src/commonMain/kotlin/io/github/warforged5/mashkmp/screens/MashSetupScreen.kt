package io.github.warforged5.mashkmp.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import io.github.warforged5.mash.MashViewModel
import io.github.warforged5.mash.navigateToMashInput
import io.github.warforged5.mashkmp.components.CategoryCard
import io.github.warforged5.mashkmp.components.CategorySelectionDialog
import io.github.warforged5.mashkmp.components.EditCategoryWithNicknameDialog
import io.github.warforged5.mashkmp.components.InfoCard
import io.github.warforged5.mashkmp.dataclasses.CategoryData
import io.github.warforged5.mashkmp.dataclasses.MashTemplate
import io.github.warforged5.mashkmp.enumclasses.MashType
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun MashSetupScreen(
    navController: NavController,
    viewModel: MashViewModel,
    type: MashType
) {
    var categoryCount by remember { mutableStateOf(8) } // Updated for new categories
    val selectedCategories = remember { mutableStateListOf<CategoryData>() }
    var showAddDialog by remember { mutableStateOf(false) }
    var editingCategory by remember { mutableStateOf<Pair<Int, CategoryData>?>(null) }
    val haptic = LocalHapticFeedback.current

    LaunchedEffect(type, categoryCount) {
        selectedCategories.clear()
        selectedCategories.addAll(
            when (type) {
                MashType.CLASSIC -> viewModel.getClassicCategories()
                MashType.HYBRID -> {
                    val classicCount = categoryCount / 2
                    val customCount = categoryCount - classicCount
                    viewModel.getClassicCategories().take(classicCount) +
                            viewModel.getRandomCategories(customCount, excludeClassic = true)
                }
                MashType.CUSTOM -> viewModel.getRandomCategories(categoryCount)
            }
        )
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Column {
                        Text("Setup $type Game")
                        Text(
                            "${selectedCategories.size} categories selected",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButton = {
            AnimatedVisibility(
                visible = selectedCategories.isNotEmpty(),
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                ExtendedFloatingActionButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        val template = MashTemplate(
                            id = "temp_${Clock.System.now().toEpochMilliseconds()}",
                            name = "$type Game",
                            categories = selectedCategories.toList(),
                            type = type
                        )
                        viewModel.tempTemplate = template
                        navController.navigateToMashInput(template.id)
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Icon(Icons.Rounded.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Start Game", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 8.dp,
                bottom = 100.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Action Buttons
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilledTonalButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                showAddDialog = true
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Rounded.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add")
                        }

                        if (type != MashType.CLASSIC) {
                            Button(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    val currentSize = selectedCategories.size
                                    val newCategories = when (type) {
                                        MashType.HYBRID -> {
                                            val classicCount = currentSize / 2
                                            val customCount = currentSize - classicCount
                                            viewModel.getClassicCategories().take(classicCount) +
                                                    viewModel.getRandomCategories(customCount, excludeClassic = true)
                                        }
                                        MashType.CUSTOM -> viewModel.getRandomCategories(currentSize)
                                        else -> emptyList()
                                    }
                                    selectedCategories.clear()
                                    selectedCategories.addAll(newCategories)
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Rounded.Refresh, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Random")
                            }
                        }
                    }
                }
            }

            if (type == MashType.HYBRID) {
                item {
                    InfoCard(
                        text = "Hybrid mode uses half classic and half custom categories",
                        icon = Icons.Rounded.Info
                    )
                }
            }

            itemsIndexed(selectedCategories) { index, categoryData ->
                CategoryCard(
                    categoryData = categoryData,
                    index = index,
                    canEdit = true,
                    canDelete = type != MashType.CLASSIC || selectedCategories.size > 8, // Updated for new classic count
                    canMoveUp = index > 0,
                    canMoveDown = index < selectedCategories.size - 1,
                    onEdit = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        editingCategory = Pair(index, categoryData)
                    },
                    onDelete = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        selectedCategories.removeAt(index)
                    },
                    onMoveUp = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        if (index > 0) {
                            val temp = selectedCategories[index]
                            selectedCategories[index] = selectedCategories[index - 1]
                            selectedCategories[index - 1] = temp
                        }
                    },
                    onMoveDown = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        if (index < selectedCategories.size - 1) {
                            val temp = selectedCategories[index]
                            selectedCategories[index] = selectedCategories[index + 1]
                            selectedCategories[index + 1] = temp
                        }
                    }
                )
            }
        }
    }

    if (showAddDialog) {
        CategorySelectionDialog(
            availableCategories = viewModel.getAllAvailableCategories().filter { category ->
                selectedCategories.none { it.realName == category.realName }
            },
            onDismiss = { showAddDialog = false },
            onCategorySelected = { categoryData ->
                selectedCategories.add(categoryData)
                showAddDialog = false
            }
        )
    }

    editingCategory?.let { (index, oldCategoryData) ->
        EditCategoryWithNicknameDialog(
            currentCategory = oldCategoryData,
            onDismiss = { editingCategory = null },
            onSave = { newCategoryData ->
                selectedCategories[index] = newCategoryData
                editingCategory = null
            }
        )
    }
}

