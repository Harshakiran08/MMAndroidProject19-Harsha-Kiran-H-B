package com.pashuaahar.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pashuaahar.ui.SharedViewModel
import com.pashuaahar.ui.theme.Strings
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(navController: NavController, sharedViewModel: SharedViewModel) {
    val logoScale = remember { Animatable(0.7f) }
    val logoAlpha = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }
    val palette = rememberFarmPalette(sharedViewModel.isDarkMode)
    val isK = sharedViewModel.isKannada

    LaunchedEffect(Unit) {
        launch { logoScale.animateTo(1f, spring()) }
        launch { logoAlpha.animateTo(1f, tween(700)) }
        launch {
            delay(250)
            textAlpha.animateTo(1f, tween(700))
        }
        delay(900L)
        val destination = if (sharedViewModel.loggedInUserId != -1) "main" else "login"
        navController.navigate(destination) {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(palette.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(0.dp),
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            Box(
                modifier = Modifier
                    .scale(logoScale.value)
                    .alpha(logoAlpha.value),
                contentAlignment = Alignment.Center
            ) {
                LogoBadge(
                    palette = palette,
                    containerSize = 228.dp,
                    logoSize = 178.dp
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.alpha(textAlpha.value)
            ) {
                Text(
                    text = Strings.get("app_name", isK),
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Black,
                    color = palette.accentStrong
                )
                Text(
                    text = Strings.get("premium_cattle_feed_planning", isK),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = palette.textSecondary
                )
            }
        }

        Text(
            text = "Version 1.0",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 28.dp)
                .alpha(textAlpha.value),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = palette.textSecondary
        )
    }
}
