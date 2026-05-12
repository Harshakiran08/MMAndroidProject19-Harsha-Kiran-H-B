package com.pashuaahar.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pashuaahar.ui.SharedViewModel
import com.pashuaahar.ui.screens.*

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

@Composable
fun AppNavigation(sharedViewModel: SharedViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController, 
        startDestination = "splash",
        enterTransition = { slideInHorizontally(animationSpec = tween(300)) { it } + fadeIn(animationSpec = tween(300)) },
        exitTransition = { slideOutHorizontally(animationSpec = tween(300)) { -it } + fadeOut(animationSpec = tween(300)) },
        popEnterTransition = { slideInHorizontally(animationSpec = tween(300)) { -it } + fadeIn(animationSpec = tween(300)) },
        popExitTransition = { slideOutHorizontally(animationSpec = tween(300)) { it } + fadeOut(animationSpec = tween(300)) }
    ) {
        composable("splash") { SplashScreen(navController, sharedViewModel) }
        composable("login") { LoginScreen(navController, sharedViewModel) }
        composable("register") { RegisterScreen(navController, sharedViewModel) }
        composable("main") { MainScreen(navController, sharedViewModel) }
        composable("input") { InputScreen(navController, sharedViewModel) }
        composable("example") { ExampleScreen(navController, sharedViewModel) }
        
        // These are screens that go "above" the bottom nav
        composable("calculator") { FeedCalculatorScreen(navController, sharedViewModel) }
        composable("comparison") { ComparisonScreen(navController, sharedViewModel) }
        composable("cost_comparison") { CostComparisonScreen(navController, sharedViewModel) }
        composable("final") { FinalScreen(navController, sharedViewModel) }
        composable("history") { HistoryScreen(navController, sharedViewModel) }
        composable("breeds") { BreedsScreen(navController, sharedViewModel) }
        composable("charts") { ChartsScreen(navController, sharedViewModel) }
        composable("settings") { SettingsScreen(sharedViewModel) }
        composable("tips") { TipsScreen(navController, sharedViewModel) }
        composable("ai_assist") { AiAssistScreen(navController, sharedViewModel) }
    }
}
