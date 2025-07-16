package io.github.warforged5.mashkmp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.warforged5.mashkmp.dataclasses.CategoryData

@Composable
fun EditCategoryWithNicknameDialog(
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
                        label = { Text("Real Name (for AI)") },
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
                    label = { Text("Display Name (shown to player)") },
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