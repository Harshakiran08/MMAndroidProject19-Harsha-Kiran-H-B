package com.pashuaahar.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pashuaahar.ui.SharedViewModel
import com.pashuaahar.ui.theme.Strings

@Composable
fun FinalScreen(navController: NavController, sharedViewModel: SharedViewModel) {
    val palette = rememberFarmPalette(sharedViewModel.isDarkMode)
    val isK = sharedViewModel.languageCode == "kn"
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { visible = true }

    FarmScrollPage(
        palette = palette,
        showTopBar = true,
        title = "Plan Completed",
        onBackClick = { navController.popBackStack() },
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp, vertical = 24.dp),
        verticalSpacing = 18.dp
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(600)) + scaleIn(animationSpec = tween(600))
        ) {
            PressableCard(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .widthIn(max = 420.dp),
                palette = palette,
                containerColor = palette.surface
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    LogoBadge(
                        palette = palette,
                        containerSize = 148.dp,
                        logoSize = 112.dp
                    )

                    Text(
                        text = Strings.get("plan_completed", isK),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = palette.textPrimary
                    )

                    Surface(shape = RoundedCornerShape(22.dp), color = palette.surfaceStrong) {
                        Text(
                            text = Strings.get("plan_completed_desc", isK),
                            modifier = Modifier.padding(18.dp),
                            fontSize = 16.sp,
                            lineHeight = 22.sp,
                            textAlign = TextAlign.Center,
                            color = palette.textSecondary
                        )
                    }

                    PlanSummaryCard(sharedViewModel = sharedViewModel, palette = palette)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SummaryChip("Flow", "Done", palette, Modifier.weight(1f))
                        SummaryChip("Tips", "Viewed", palette, Modifier.weight(1f))
                        SummaryChip("Plan", "Saved", palette, Modifier.weight(1f))
                    }
                    ResultActionsCard(
                        navController = navController,
                        sharedViewModel = sharedViewModel,
                        palette = palette,
                        primaryActionLabel = "Calculate another",
                        onPrimaryAction = {
                            sharedViewModel.resetCalculatorFlow()
                            navController.navigate("main") {
                                popUpTo("main") { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SummaryChip(title: String, value: String, palette: FarmPalette, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = palette.surfaceStrong
    ) {
        Column(
            modifier = Modifier.padding(vertical = 14.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(title, color = palette.textSecondary, fontSize = 12.sp)
            Text(value, color = palette.accentStrong, fontWeight = FontWeight.Black, fontSize = 16.sp)
        }
    }
}
