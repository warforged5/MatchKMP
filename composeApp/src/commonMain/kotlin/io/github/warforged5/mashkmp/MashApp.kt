package io.github.warforged5.mash
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import androidx.navigation.compose.*
import io.github.warforged5.mashkmp.screens.CreateTemplateScreen
import io.github.warforged5.mash.screens.HistoryScreen
import io.github.warforged5.mash.screens.HomeScreen
import io.github.warforged5.mash.screens.MashInputScreen
import io.github.warforged5.mash.screens.MashResultScreen
import io.github.warforged5.mash.screens.MashSetupScreen
import io.github.warforged5.mash.screens.NewMashScreen
import io.github.warforged5.mash.ui.theme.ThemeManager
import io.github.warforged5.mashkmp.enumclasses.MashType
import io.github.warforged5.mashkmp.functions.rememberMashViewModel

@Composable
fun MashApp(themeManager: ThemeManager) {
    val navController = rememberNavController()
    val viewModel = rememberMashViewModel()

    NavHost(
        navController = navController,
        startDestination = "home",
        enterTransition = {
            fadeIn(animationSpec = tween(400, easing = FastOutSlowInEasing)) +
                    slideInHorizontally(animationSpec = tween(400, easing = FastOutSlowInEasing)) { it / 4 } +
                    scaleIn(initialScale = 0.9f, animationSpec = tween(400, easing = FastOutSlowInEasing))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(200)) +
                    slideOutHorizontally(animationSpec = tween(200)) { -it / 4 }
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(400, easing = FastOutSlowInEasing)) +
                    slideInHorizontally(animationSpec = tween(400, easing = FastOutSlowInEasing)) { -it / 4 }
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(200)) +
                    slideOutHorizontally(animationSpec = tween(200)) { it / 4 } +
                    scaleOut(targetScale = 0.9f, animationSpec = tween(200))
        }
    ) {
        composable("home") { HomeScreen(navController, themeManager) }
        composable("new_mash") { NewMashScreen(navController, viewModel) }
        composable("create_template") { CreateTemplateScreen(navController, viewModel) }
        composable("history") { HistoryScreen(navController, viewModel) }
        composable("mash_setup/{type}") { backStackEntry ->
            val type = MashType.valueOf(backStackEntry.arguments?.getString("type") ?: "CLASSIC")
            MashSetupScreen(navController, viewModel, type)
        }
        composable("mash_input/{templateId}") { backStackEntry ->
            val templateId = backStackEntry.arguments?.getString("templateId") ?: ""
            MashInputScreen(navController, viewModel, templateId)
        }
        composable("mash_result") {
            MashResultScreen(navController, viewModel)
        }
    }
}