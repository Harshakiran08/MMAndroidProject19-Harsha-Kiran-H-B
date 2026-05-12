package com.pashuaahar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pashuaahar.ui.SharedViewModel
import com.pashuaahar.ui.theme.Strings

@Composable
fun ExampleScreen(navController: androidx.navigation.NavController, sharedViewModel: SharedViewModel) {
    val palette = rememberFarmPalette(sharedViewModel.isDarkMode)
    val isK = sharedViewModel.isKannada

    val steps = listOf(
        Triple("01", Strings.get("cow_profile_input", isK), "Capture the cow profile, breed, body weight, and target milk output."),
        Triple("02", Strings.get("recommended_feed_mix", isK), "Let the calculator suggest a custom blend using the saved market assumptions."),
        Triple("03", Strings.get("cost_comparison", isK), "Review homemade versus market feed cost and keep the best option in history.")
    )

    FarmScrollPage(
        palette = palette,
        showTopBar = true,
        title = "User Guide",
        onBackClick = { navController.popBackStack() }
    ) {
        Text(
            text = Strings.get("how_to_use", isK),
            color = palette.textPrimary,
            fontSize = 30.sp,
            fontWeight = FontWeight.Black
        )
        Text(
            text = "A short operator guide for first-time users and quick onboarding in the field.",
            color = palette.textSecondary,
            lineHeight = 22.sp
        )

        steps.forEach { (number, title, body) ->
            Card(
                shape = farmCardShape(),
                colors = CardDefaults.cardColors(containerColor = palette.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .background(palette.accentSoft, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = number,
                            color = palette.accentStrong,
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(text = title, color = palette.textPrimary, fontWeight = FontWeight.Bold, fontSize = 19.sp)
                        Text(text = body, color = palette.textSecondary, lineHeight = 20.sp)
                    }
                }
            }
        }
    }
}
