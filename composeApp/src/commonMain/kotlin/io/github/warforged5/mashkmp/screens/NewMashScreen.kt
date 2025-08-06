package io.github.warforged5.mashkmp.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import io.github.warforged5.mash.navigateToMashInput
import io.github.warforged5.mash.navigateToMashSetup
import io.github.warforged5.mashkmp.MashViewModel
import io.github.warforged5.mashkmp.components.GameTypeCard
import io.github.warforged5.mashkmp.components.TemplateSelectionDialog
import io.github.warforged5.mashkmp.dataclasses.PremadeTemplate
import io.github.warforged5.mashkmp.dataclasses.PremadeTemplates
import io.github.warforged5.mashkmp.dataclasses.TemplateCategory
import io.github.warforged5.mashkmp.enumclasses.MashType
import kotlinx.datetime.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun NewMashScreen(navController: NavController, viewModel: MashViewModel) {
    var selectedTab by remember { mutableStateOf(0) }
    var showTemplateDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<TemplateCategory?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var showTemplatePreview by remember { mutableStateOf<PremadeTemplate?>(null) }
    val haptic = LocalHapticFeedback.current

    // Get current month for recommendations
    val currentMonth = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).monthNumber
    val recommendedTemplates = remember { PremadeTemplates.getRecommendedTemplates(currentMonth) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Choose Your Adventure",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        AnimatedVisibility(visible = selectedTab == 1) {
                            Text(
                                "${PremadeTemplates.allPremadeTemplates.size} templates available",
                                style = MaterialTheme.typography.bodySmall,
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                )
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Enhanced Tab Row
            EnhancedTabRow(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )

            // Tab Content
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    fadeIn(tween(300)) + slideInHorizontally(tween(300)) { if (targetState > initialState) it else -it } togetherWith
                            fadeOut(tween(200)) + slideOutHorizontally(tween(200)) { if (targetState > initialState) -it else it }
                }
            ) { tab ->
                when (tab) {
                    0 -> QuickStartTab(navController, viewModel)
                    1 -> PremadeTemplatesTab(
                        navController = navController,
                        viewModel = viewModel,
                        selectedCategory = selectedCategory,
                        onCategorySelected = { selectedCategory = it },
                        searchQuery = searchQuery,
                        onSearchQueryChange = { searchQuery = it },
                        onTemplatePreview = { showTemplatePreview = it },
                        recommendedTemplates = recommendedTemplates,

                    )
                    2 -> CustomCreateTab(navController)
                }
            }
        }
    }

    // Template Preview Dialog
    showTemplatePreview?.let { template ->
        TemplatePreviewDialog(
            template = template,
            onDismiss = { showTemplatePreview = null },
            onPlay = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                viewModel.tempTemplate = template.template
                navController.navigateToMashInput(template.template.id)
            }
        )
    }

    // Saved Templates Dialog
    if (showTemplateDialog) {
        TemplateSelectionDialog(
            templates = viewModel.templates,
            onDismiss = { showTemplateDialog = false },
            onTemplateSelected = { template ->
                showTemplateDialog = false
                navController.navigateToMashInput(template.id)
            }
        )
    }
}

@Composable
private fun EnhancedTabRow(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val tabs = listOf(
        "Quick Start" to Icons.Rounded.PlayArrow,
        "Templates" to Icons.Rounded.Dashboard,
        "Custom" to Icons.Rounded.Build
    )

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurface,
            indicator = { tabPositions ->
                Box(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedTab])
                        .height(3.dp)
                        .padding(horizontal = 24.dp)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp)
                        )
                )
            }
        ) {
            tabs.forEachIndexed { index, (title, icon) ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { onTabSelected(index) },
                    modifier = Modifier.height(64.dp).padding(horizontal = 16.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            icon,
                            contentDescription = title,
                            modifier = Modifier.size(20.dp),
                            tint = if (selectedTab == index)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickStartTab(
    navController: NavController,
    viewModel: MashViewModel,
) {
    LazyColumn(
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Saved Templates Quick Access
        if (viewModel.templates.isNotEmpty()) {
            item {
                SavedTemplatesSection(
                    templates = viewModel.templates.take(3),
                    totalCount = viewModel.templates.size,
                    onTemplateClick = { template ->

                        navController.navigateToMashInput(template.id)
                    },
                    onSeeAllClick = {
                        // Show template dialog
                    }
                )
            }

            item {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                )
            }
        }

        // Classic Game Types
        item {
            Text(
                "Game Modes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        item {
            GameTypeCard(
                type = MashType.CLASSIC,
                title = "Classic MASH",
                description = "The original game you know and love",
                icon = Icons.Rounded.Home,
                gradient = listOf(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.3f)
                ),
                delay = 0,
                onClick = {

                    navController.navigateToMashSetup(MashType.CLASSIC)
                }
            )
        }

        item {
            GameTypeCard(
                type = MashType.HYBRID,
                title = "Hybrid Mix",
                description = "Best of both worlds",
                icon = Icons.Rounded.Shuffle,
                gradient = listOf(
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                    MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.3f)
                ),
                delay = 100,
                onClick = {

                    navController.navigateToMashSetup(MashType.HYBRID)
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PremadeTemplatesTab(
    navController: NavController,
    viewModel: MashViewModel,
    selectedCategory: TemplateCategory?,
    onCategorySelected: (TemplateCategory?) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onTemplatePreview: (PremadeTemplate) -> Unit,
    recommendedTemplates: List<PremadeTemplate>,
) {
    val filteredTemplates = remember(selectedCategory, searchQuery) {
        when {
            searchQuery.isNotBlank() -> PremadeTemplates.searchTemplates(searchQuery)
            selectedCategory != null -> PremadeTemplates.getTemplatesByCategory(selectedCategory)
            else -> PremadeTemplates.allPremadeTemplates
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Search Bar
        item {
            SearchBar(
                query = searchQuery,
                onQueryChange = onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }

        // Category Chips
        item {
            CategoryChipsRow(
                selectedCategory = selectedCategory,
                onCategorySelected = onCategorySelected
            )
        }

        // Recommended Section (if no filters)
        if (selectedCategory == null && searchQuery.isBlank() && recommendedTemplates.isNotEmpty()) {
            item {
                RecommendedSection(
                    templates = recommendedTemplates,
                    onTemplateClick = onTemplatePreview
                )
            }
        }

        // Templates Grid Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    when {
                        searchQuery.isNotBlank() -> "Search Results"
                        selectedCategory != null -> selectedCategory.name.replace("_", " ")
                        else -> "All Templates"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "${filteredTemplates.size} templates",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Templates Grid
        items(
            filteredTemplates.chunked(2),
            key = { it.joinToString { template -> template.template.id } }
        ) { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                row.forEach { template ->
                    TemplateCard(
                        template = template,
                        onClick = { onTemplatePreview(template) },
                        modifier = Modifier.weight(1f)
                    )
                }
                // Add empty space if odd number
                if (row.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun CustomCreateTab(
    navController: NavController,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Animated Icon
        val infiniteTransition = rememberInfiniteTransition()
        val scale by infiniteTransition.animateFloat(
            initialValue = 0.95f,
            targetValue = 1.05f,
            animationSpec = infiniteRepeatable(
                tween(2000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )

        Surface(
            modifier = Modifier
                .size(120.dp)
                .scale(scale),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.tertiaryContainer
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Rounded.Build,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            "Create Your Own",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            "Design a completely custom MASH game",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 40.dp, vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                navController.navigateToMashSetup(MashType.CUSTOM)
            },
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(56.dp)
        ) {
            Icon(Icons.Rounded.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Start Creating", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        placeholder = { Text("Search templates...") },
        leadingIcon = {
            Icon(Icons.Rounded.Search, contentDescription = "Search")
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Rounded.Clear, contentDescription = "Clear")
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        )
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CategoryChipsRow(
    selectedCategory: TemplateCategory?,
    onCategorySelected: (TemplateCategory?) -> Unit
) {
    val categories = TemplateCategory.values()

    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // All chip
        item {
            FilterChip(
                selected = selectedCategory == null,
                onClick = { onCategorySelected(null) },
                label = { Text("All") },
                leadingIcon = if (selectedCategory == null) {
                    { Icon(Icons.Rounded.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                } else null
            )
        }

        // Category chips
        items(categories) { category ->
            FilterChip(
                selected = selectedCategory == category,
                onClick = {
                    onCategorySelected(
                        if (selectedCategory == category) null else category
                    )
                },
                label = {
                    Text(
                        category.name.lowercase().replaceFirstChar { it.uppercase() }
                            .replace("_", " ")
                    )
                },
                leadingIcon = {
                    Text(getCategoryEmoji(category))
                }
            )
        }
    }
}

@Composable
private fun RecommendedSection(
    templates: List<PremadeTemplate>,
    onTemplateClick: (PremadeTemplate) -> Unit
) {
    Column(
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Rounded.AutoAwesome,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    "Recommended for You",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(templates) { template ->
                RecommendedTemplateCard(
                    template = template,
                    onClick = { onTemplateClick(template) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TemplateCard(
    template: PremadeTemplate,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier.height(180.dp),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top
            ) {
                Surface(
                    modifier = Modifier.size(40.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = getCategoryColor(template.category).copy(alpha = 0.2f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            getCategoryEmoji(template.category),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        template.template.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Description
            Text(
                template.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Footer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Difficulty indicator
                DifficultyIndicator(template.difficulty)

                // Category count
                Text(
                    "${template.template.categories.size} categories",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecommendedTemplateCard(
    template: PremadeTemplate,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier
            .width(200.dp)
            .height(140.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    getCategoryEmoji(template.category),
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    template.template.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }

            Text(
                template.description,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DifficultyIndicator(template.difficulty)
                Icon(
                    Icons.Rounded.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun SavedTemplatesSection(
    templates: List<io.github.warforged5.mashkmp.dataclasses.MashTemplate>,
    totalCount: Int,
    onTemplateClick: (io.github.warforged5.mashkmp.dataclasses.MashTemplate) -> Unit,
    onSeeAllClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Your Templates",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            if (totalCount > 3) {
                TextButton(onClick = onSeeAllClick) {
                    Text("See All ($totalCount)")
                    Icon(
                        Icons.Rounded.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        templates.forEach { template ->
            QuickTemplateCard(
                template = template,
                onClick = { onTemplateClick(template) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuickTemplateCard(
    template: io.github.warforged5.mashkmp.dataclasses.MashTemplate,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                when (template.type) {
                    MashType.CLASSIC -> Icons.Rounded.Home
                    MashType.HYBRID -> Icons.Rounded.Shuffle
                    MashType.CUSTOM -> Icons.Rounded.Build
                },
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    template.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "${template.categories.size} categories",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                Icons.Rounded.PlayArrow,
                contentDescription = "Play",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun DifficultyIndicator(level: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        repeat(3) { index ->
            Surface(
                modifier = Modifier.size(4.dp, 12.dp),
                shape = RoundedCornerShape(2.dp),
                color = if (index < level)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            ) {}
        }
    }
}

@Composable
private fun TemplatePreviewDialog(
    template: PremadeTemplate,
    onDismiss: () -> Unit,
    onPlay: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f),
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            getCategoryColor(template.category).copy(alpha = 0.1f)
                        )
                        .padding(24.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Surface(
                                modifier = Modifier.size(56.dp),
                                shape = RoundedCornerShape(16.dp),
                                color = getCategoryColor(template.category).copy(alpha = 0.2f)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        getCategoryEmoji(template.category),
                                        style = MaterialTheme.typography.headlineMedium
                                    )
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    template.template.name,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    template.description,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            IconButton(onClick = onDismiss) {
                                Icon(Icons.Rounded.Close, contentDescription = "Close")
                            }
                        }

                        // Tags
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(template.tags) { tag ->
                                AssistChip(
                                    onClick = { },
                                    label = { Text(tag) },
                                    modifier = Modifier.height(28.dp)
                                )
                            }
                        }
                    }
                }

                // Categories List
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Categories",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            DifficultyIndicator(template.difficulty)
                        }
                    }

                    items(template.template.categories) { category ->
                        CategoryPreviewCard(category)
                    }
                }

                // Action Buttons
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel")
                        }

                        Button(
                            onClick = onPlay,
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

@Composable
private fun CategoryPreviewCard(
    category: io.github.warforged5.mashkmp.dataclasses.CategoryData
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                category.icon,
                style = MaterialTheme.typography.titleLarge
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    category.realName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                if (category.nickname != category.realName) {
                    Text(
                        "Shows as: ${category.nickname}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

private fun getCategoryEmoji(category: TemplateCategory): String = when (category) {
    TemplateCategory.ROMANCE -> "💕"
    TemplateCategory.CAREER -> "💼"
    TemplateCategory.LIFESTYLE -> "🏡"
    TemplateCategory.ADVENTURE -> "🌍"
    TemplateCategory.FAMILY -> "👨‍👩‍👧‍👦"
    TemplateCategory.EDUCATION -> "🎓"
    TemplateCategory.SEASONAL -> "🎄"
    TemplateCategory.FANTASY -> "🦄"
    TemplateCategory.CELEBRITY -> "⭐"
    TemplateCategory.RETIREMENT -> "🏖️"
}

private fun getCategoryColor(category: TemplateCategory): Color = when (category) {
    TemplateCategory.ROMANCE -> Color(0xFFE91E63)
    TemplateCategory.CAREER -> Color(0xFF2196F3)
    TemplateCategory.LIFESTYLE -> Color(0xFF4CAF50)
    TemplateCategory.ADVENTURE -> Color(0xFFFF9800)
    TemplateCategory.FAMILY -> Color(0xFF9C27B0)
    TemplateCategory.EDUCATION -> Color(0xFF00BCD4)
    TemplateCategory.SEASONAL -> Color(0xFFFF5722)
    TemplateCategory.FANTASY -> Color(0xFF673AB7)
    TemplateCategory.CELEBRITY -> Color(0xFFFFC107)
    TemplateCategory.RETIREMENT -> Color(0xFF795548)
}