package io.github.warforged5.mashkmp.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import io.github.warforged5.mash.Home
import io.github.warforged5.mash.MashViewModel
import io.github.warforged5.mashkmp.components.ChoiceInputSection
import io.github.warforged5.mashkmp.components.NewEliminationSystem
import io.github.warforged5.mashkmp.components.NewSpiralDrawingDialog
import io.github.warforged5.mashkmp.components.OutputTypeDialog
import io.github.warforged5.mashkmp.dataclasses.MashResult
import io.github.warforged5.mashkmp.enumclasses.OutputType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MashInputScreen(
    navController: NavController,
    viewModel: MashViewModel,
    templateId: String
) {
    val template = if (templateId.startsWith("temp_")) {
        viewModel.tempTemplate
    } else {
        viewModel.templates.find { it.id == templateId }
    } ?: return

    val userChoices = remember { mutableStateMapOf<String, SnapshotStateList<String>>() }
    var currentCategoryIndex by remember { mutableStateOf(0) }
    var showOutputDialog by remember { mutableStateOf(false) }
    var outputType by remember { mutableStateOf<OutputType?>(null) }
    var isGeneratingStory by remember { mutableStateOf(false) }
    var isGeneratingStoryAfterSpiral by remember { mutableStateOf(false) }
    var showSpiralDrawing by remember { mutableStateOf(false) }
    var showNewElimination by remember { mutableStateOf(false) }
    var spiralCount by remember { mutableStateOf(0) }
    var finalSelections by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    val coroutineScope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current

    val currentCategoryData = template.categories.getOrNull(currentCategoryIndex)
    val currentCategory = currentCategoryData
    val currentCategoryNickname = currentCategoryData?.nickname ?: ""
    val choices = remember(currentCategory) {
        userChoices.getOrPut(currentCategory?.realName ?: "") { mutableStateListOf() }
    }

    LaunchedEffect(currentCategory) {
        if (currentCategory?.realName ?: "" == "MASH (Housing)" && choices.isEmpty()) {
            choices.addAll(listOf("Mansion", "Shack", "Apartment", "House"))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") }, // Empty title, we'll use the custom header below
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Rounded.Close, contentDescription = "Close")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        bottomBar = {
            if (!showNewElimination) {
                Surface(
                    tonalElevation = 0.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AnimatedVisibility(
                            visible = currentCategoryIndex > 0,
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    if (currentCategoryIndex > 0) {
                                        currentCategoryIndex--
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Previous")
                            }
                        }

                        Button(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                if (currentCategoryIndex < template.categories.size - 1) {
                                    currentCategoryIndex++
                                } else {
                                    showOutputDialog = true
                                }
                            },
                            enabled = choices.size >= 2,
                            modifier = Modifier.weight(if (currentCategoryIndex > 0) 1f else 2f)
                        ) {
                            Text(
                                if (currentCategoryIndex < template.categories.size - 1) "Next" else "Finish"
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                if (currentCategoryIndex < template.categories.size - 1)
                                    Icons.AutoMirrored.Rounded.ArrowForward
                                else
                                    Icons.Rounded.Done,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        if (showNewElimination) {
            // Show new elimination system
            NewEliminationSystem(
                allChoices = userChoices.mapKeys {
                    template.categories.find { category -> category.realName == it.key }!!
                },
                spiralCount = spiralCount,
                onComplete = { results ->
                    val realNameResults = mutableMapOf<String, String>()
                    results.forEach { (category, selection) ->
                        realNameResults[category] = selection
                    }

                    val result = MashResult(
                        template = template,
                        allChoices = userChoices.mapValues { it.value.toList() },
                        selections = realNameResults,
                        spiralCount = spiralCount
                    )
                    viewModel.saveResult(result)
                    navController.navigate(MashResult) {
                        popUpTo<Home>()
                    }
                },
                onGenerateStory = { results ->
                    val realNameResults = mutableMapOf<String, String>()
                    results.forEach { (category, selection) ->
                        realNameResults[category] = selection
                    }

                    isGeneratingStoryAfterSpiral = true
                    coroutineScope.launch {
                        val story = viewModel.generateAIStory(realNameResults)
                        val result = MashResult(
                            template = template,
                            allChoices = userChoices.mapValues { it.value.toList() },
                            selections = realNameResults,
                            story = story,
                            spiralCount = spiralCount
                        )
                        viewModel.saveResult(result)
                        isGeneratingStoryAfterSpiral = false
                        navController.navigate(MashResult) {
                            popUpTo<Home>()
                        }
                    }
                }
            )
        } else {
            // Normal input content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Enhanced Header Container
                Surface(
                    modifier = Modifier.fillMaxWidth().padding(10.dp),
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                    tonalElevation = 0.dp
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // Progress Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    "Fill in Choices",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    "Step ${currentCategoryIndex + 1} of ${template.categories.size}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                border = BorderStroke(
                                    1.dp,
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                                )
                            ) {
                                Text(
                                    "${currentCategoryIndex + 1}/${template.categories.size}",
                                    modifier = Modifier.padding(
                                        horizontal = 16.dp,
                                        vertical = 8.dp
                                    ),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Enhanced Progress Bar Container
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            "Progress",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            "${((currentCategoryIndex + 1) / template.categories.size.toFloat() * 100).toInt()}%",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }

                                    LinearProgressIndicator(
                                        progress = (currentCategoryIndex + 1) / template.categories.size.toFloat(),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(8.dp)
                                            .clip(RoundedCornerShape(4.dp)),
                                        color = MaterialTheme.colorScheme.primary,
                                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                                        strokeCap = StrokeCap.Round
                                    )
                                }
                            }

                            // Current Category Display
                            val bounce by animateFloatAsState(
                                targetValue = 1f,
                                animationSpec = spring(
                                    dampingRatio = 0.3f,
                                    stiffness = Spring.StiffnessLow
                                )
                            )

                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                border = BorderStroke(
                                    1.dp,
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        currentCategoryNickname,
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.scale(bounce)
                                    )

                                    Surface(
                                        shape = RoundedCornerShape(20.dp),
                                        color = MaterialTheme.colorScheme.primaryContainer.copy(
                                            alpha = 0.7f
                                        )
                                    ) {
                                        Text(
                                            "Add 2-6 choices",
                                            modifier = Modifier.padding(
                                                horizontal = 16.dp,
                                                vertical = 6.dp
                                            ),
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Input Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ChoiceInputSection(
                        categoryName = currentCategory?.nickname ?: "",
                        choices = choices.toList(),
                        onAddChoice = { choice ->
                            if (choice.isNotBlank() && choices.size < 6) {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                choices.add(choice)
                            }
                        },
                        onRemoveChoice = { index ->
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            choices.removeAt(index)
                        }
                    )

                    // Help Text
                    AnimatedVisibility(
                        visible = choices.size < 2,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    Icons.Rounded.Info,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    "Add at least 2 choices to continue",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }

        // Dialogs and overlays
        if (showSpiralDrawing) {
            NewSpiralDrawingDialog(
                onCountComplete = { count ->
                    spiralCount = count
                    showSpiralDrawing = false
                    showNewElimination = true
                },
                onDismiss = { showSpiralDrawing = false }
            )
        }

        // Show loading overlay when generating story
        if (isGeneratingStory || isGeneratingStoryAfterSpiral) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.8f))
                    .clickable(enabled = false) { }, // Prevent clicks
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier.padding(32.dp),
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 0.dp
                ) {
                    Column(
                        modifier = Modifier.padding(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        // Animated loading indicator
                        val infiniteTransition = rememberInfiniteTransition()
                        val rotationAngle by infiniteTransition.animateFloat(
                            initialValue = 0f,
                            targetValue = 360f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(2000, easing = LinearEasing)
                            )
                        )

                        Box(
                            modifier = Modifier.size(80.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(60.dp)
                                    .rotate(rotationAngle),
                                strokeWidth = 4.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Surface(
                                modifier = Modifier.size(40.dp),
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        if (isGeneratingStoryAfterSpiral) "🌀" else "✨",
                                        style = MaterialTheme.typography.headlineSmall
                                    )
                                }
                            }
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                if (isGeneratingStoryAfterSpiral) "Weaving Your Spiral Story" else "Creating Your Story",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                if (isGeneratingStoryAfterSpiral)
                                    "The spiral's magic is creating your tale..."
                                else
                                    "Our AI is crafting your personalized future...",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        if (showOutputDialog) {
            OutputTypeDialog(
                onDismiss = { showOutputDialog = false },
                onOutputTypeSelected = { selected ->
                    outputType = selected
                    showOutputDialog = false

                    when (selected) {
                        OutputType.SPIRAL -> {
                            showSpiralDrawing = true
                        }

                        OutputType.AI_STORY -> {
                            isGeneratingStory = true
                            // Make random selections for AI story
                            val selections = mutableMapOf<String, String>()
                            template.categories.forEach { categoryData ->
                                val categoryChoices = userChoices[categoryData.realName] ?: listOf()
                                if (categoryChoices.isNotEmpty()) {
                                    selections[categoryData.realName] = categoryChoices.random()
                                }
                            }

                            coroutineScope.launch {
                                val story = viewModel.generateAIStory(selections)
                                val result = MashResult(
                                    template = template,
                                    allChoices = userChoices.mapValues { it.value.toList() },
                                    selections = selections,
                                    story = story
                                )
                                viewModel.saveResult(result)
                                isGeneratingStory = false
                                navController.navigate(MashResult) {
                                    popUpTo<Home>()
                                }
                            }
                        }

                        OutputType.AUTOMATIC -> {
                            // Make random selections for automatic
                            val selections = mutableMapOf<String, String>()
                            template.categories.forEach { categoryData ->
                                val categoryChoices = userChoices[categoryData.realName] ?: listOf()
                                if (categoryChoices.isNotEmpty()) {
                                    selections[categoryData.realName] = categoryChoices.random()
                                }
                            }

                            val result = MashResult(
                                template = template,
                                allChoices = userChoices.mapValues { it.value.toList() },
                                selections = selections
                            )
                            viewModel.saveResult(result)
                            navController.navigate(MashResult) {
                                popUpTo<Home>()
                            }
                        }
                    }
                }
            )
        }
    }
}