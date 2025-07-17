package io.github.warforged5.mashkmp.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Sort
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import io.github.warforged5.mash.MashViewModel
import io.github.warforged5.mash.navigateToNewMash
import io.github.warforged5.mashkmp.components.EmptyHistoryState
import io.github.warforged5.mashkmp.components.ModernHistoryCard
import io.github.warforged5.mashkmp.enumclasses.MashType


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController, viewModel: MashViewModel) {
    var filterType by remember { mutableStateOf<MashType?>(null) }
    var showStoryOnly by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    val filteredHistory = remember(filterType, showStoryOnly, viewModel.history.size) {
        viewModel.history.filter { result ->
            (filterType == null || result.template.type == filterType) &&
                    (!showStoryOnly || result.story != null)
        }
    }

    val pullToRefreshState = rememberPullToRefreshState()
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Column {
                        Text("History")
                        AnimatedContent(
                            targetState = filteredHistory.size,
                            transitionSpec = {
                                fadeIn() + slideInVertically() togetherWith fadeOut() + slideOutVertically()
                            }
                        ) { count ->
                            Text(
                                "$count ${if (count == 1) "game" else "games"} played",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { paddingValues ->
        Box(

        ) {
            if (viewModel.history.isEmpty()) {
                EmptyHistoryState(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    onPlayClick = { navController.navigateToNewMash()}
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // Filter Chips
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            FilterChip(
                                selected = filterType == null && !showStoryOnly,
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    filterType = null
                                    showStoryOnly = false
                                },
                                label = { Text("All") },
                                leadingIcon = if (filterType == null && !showStoryOnly) {
                                    { Icon(Icons.Rounded.Done, contentDescription = null, modifier = Modifier.size(16.dp)) }
                                } else null
                            )
                        }

                        item {
                            FilterChip(
                                selected = showStoryOnly,
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    showStoryOnly = !showStoryOnly
                                },
                                label = { Text("With Stories") },
                                leadingIcon = {
                                    Icon(
                                        if (showStoryOnly) Icons.Rounded.Done else Icons.Rounded.AutoStories,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            )
                        }

                        MashType.values().forEach { type ->
                            item {
                                FilterChip(
                                    selected = filterType == type,
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        filterType = if (filterType == type) null else type
                                    },
                                    label = { Text(type.name.lowercase().replaceFirstChar { it.uppercase() }) },
                                    leadingIcon = if (filterType == type) {
                                        { Icon(Icons.Rounded.Done, contentDescription = null, modifier = Modifier.size(16.dp)) }
                                    } else null
                                )
                            }
                        }
                    }

                    // Results List
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (filteredHistory.isEmpty()) {
                            item {
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant
                                ) {
                                    Box(
                                        modifier = Modifier.padding(32.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            "No results match your filters",
                                            style = MaterialTheme.typography.bodyLarge,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        } else {
                            itemsIndexed(filteredHistory) { index, result ->
                                ModernHistoryCard(
                                    result = result,
                                    index = index,
                                    onDelete = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        viewModel.deleteResult(result)
                                    },
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        // Navigate to detailed view or expand
                                    }
                                )
                            }
                        }
                    }
                }
            }

            PullToRefreshState(
            )
        }
    }
}
