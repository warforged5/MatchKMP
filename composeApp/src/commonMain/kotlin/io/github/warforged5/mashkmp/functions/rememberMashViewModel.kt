package io.github.warforged5.mashkmp.functions

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import io.github.warforged5.mash.MashViewModel

@Composable
fun rememberMashViewModel(): MashViewModel {
    val context = LocalContext.current
    return remember { MashViewModel(context) }
}