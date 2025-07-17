package io.github.warforged5.mash

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import io.github.warforged5.mashkmp.screens.CreateTemplateScreen
import io.github.warforged5.mashkmp.screens.HistoryScreen
import io.github.warforged5.mashkmp.screens.HomeScreen
import io.github.warforged5.mashkmp.screens.MashInputScreen
import io.github.warforged5.mashkmp.screens.MashResultScreen
import io.github.warforged5.mashkmp.screens.MashSetupScreen
import io.github.warforged5.mashkmp.screens.NewMashScreen
import io.github.warforged5.mash.ui.theme.ThemeManager
import io.github.warforged5.mashkmp.enumclasses.MashType
import io.github.warforged5.mashkmp.functions.rememberMashViewModel

// Define type-safe navigation destinations
@Serializable
object Home

@Serializable
object NewMash

@Serializable
object CreateTemplate

@Serializable
object History

@Serializable
data class MashSetup(val type: String)

@Serializable
data class MashInput(val templateId: String)

@Serializable
object ResultScreen  // Renamed from MashResult to avoid collision

@Composable
fun MashApp(themeManager: ThemeManager) {
    val navController = rememberNavController()
    val viewModel = rememberMashViewModel()

    NavHost(
        navController = navController,
        startDestination = Home,
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
        composable<Home> {
            HomeScreen(navController, themeManager)
        }

        composable<NewMash> {
            NewMashScreen(navController, viewModel)
        }

        composable<CreateTemplate> {
            CreateTemplateScreen(navController, viewModel)
        }

        composable<History> {
            HistoryScreen(navController, viewModel)
        }

        composable<MashSetup> { backStackEntry ->
            val mashSetup: MashSetup = backStackEntry.toRoute()
            val type = try {
                MashType.valueOf(mashSetup.type)
            } catch (e: IllegalArgumentException) {
                MashType.CLASSIC
            }
            MashSetupScreen(navController, viewModel, type)
        }

        composable<MashInput> { backStackEntry ->
            val mashInput: MashInput = backStackEntry.toRoute()
            MashInputScreen(navController, viewModel, mashInput.templateId)
        }

        composable<ResultScreen> {  // Updated route name
            MashResultScreen(navController, viewModel)
        }
    }
}

// Extension functions to help with navigation
fun NavController.navigateToMashSetup(type: MashType) {
    navigate(MashSetup(type.name))
}

fun NavController.navigateToMashInput(templateId: String) {
    navigate(MashInput(templateId))
}

fun NavController.navigateToNewMash() {
    navigate(NewMash)
}

fun NavController.navigateToCreateTemplate() {
    navigate(CreateTemplate)
}

fun NavController.navigateToHistory() {
    navigate(History)
}

fun NavController.navigateToMashResult() {
    navigate(ResultScreen)  // Updated to use new name
}

fun NavController.navigateToHome() {
    navigate(Home)
}