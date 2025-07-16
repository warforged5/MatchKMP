package io.github.warforged5.mashkmp.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material.icons.rounded.TouchApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Dialog
import io.github.warforged5.mashkmp.enumclasses.OutputType
import io.github.warforged5.mashkmp.components.OutputOptionCard

@Composable
fun OutputTypeDialog(
    onDismiss: () -> Unit,
    onOutputTypeSelected: (OutputType) -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    Dialog(onDismissRequest = { if (!isLoading) onDismiss() }) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            AnimatedContent(
                targetState = isLoading,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                }
            ) { loading ->
                if (loading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            CircularProgressIndicator()
                            Text(
                                "Creating your story...",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            "Choose Your Fortune Method",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        OutputOptionCard(
                            title = "Classic Mash",
                            description = "See the future, though drawing",
                            icon = Icons.Rounded.TouchApp,
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,

                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onOutputTypeSelected(OutputType.SPIRAL)
                            }
                        )


                        OutputOptionCard(
                            title = "Quick Results",
                            description = "Quickly see your future",
                            icon = Icons.Rounded.AutoAwesome,
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onOutputTypeSelected(OutputType.AUTOMATIC)
                            }
                        )



                        OutputOptionCard(
                            title = "AI Story",
                            description = "Get a personalized story about your future",
                            icon = Icons.Rounded.Psychology,
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                isLoading = true
                                onOutputTypeSelected(OutputType.AI_STORY)
                            }
                        )
                    }
                }
            }
        }
    }
}