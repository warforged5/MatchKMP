package io.github.warforged5.mashkmp.functions

import androidx.compose.runtime.*
import io.github.warforged5.mashkmp.MashViewModel
import io.github.warforged5.mashkmp.platform.createSettings

@Composable
fun rememberMashViewModel(): MashViewModel {
    val settings = createSettings()
    return remember { MashViewModel(settings) }
}