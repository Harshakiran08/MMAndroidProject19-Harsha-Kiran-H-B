package com.pashuaahar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pashuaahar.ui.SharedViewModel
import com.pashuaahar.ui.theme.Strings

@Composable
fun BreedsScreen(navController: androidx.navigation.NavController, sharedViewModel: SharedViewModel) {
    val isK = sharedViewModel.isKannada
    val palette = rememberFarmPalette(sharedViewModel.isDarkMode)
    FarmScrollPage(
        palette = palette,
        showTopBar = true,
        title = Strings.get("cattle_breeds", isK),
        onBackClick = { navController.popBackStack() }
    ) {
        BreedCard(
            title = "Jersey",
            description = Strings.get("jersey_desc", isK),
            iconColor = palette.highlight,
            icon = "Cow",
            palette = palette
        )

        BreedCard(
            title = "HF (Holstein Friesian)",
            description = Strings.get("hf_desc", isK),
            iconColor = palette.accent,
            icon = "HF",
            palette = palette
        )

        BreedCard(
            title = "Desi",
            description = Strings.get("desi_desc", isK),
            iconColor = palette.accentStrong,
            icon = "IN",
            palette = palette
        )
    }
}

@Composable
fun BreedCard(
    title: String,
    description: String,
    iconColor: Color,
    icon: String,
    palette: FarmPalette
) {
    PressableCard(
        modifier = Modifier.fillMaxWidth(),
        palette = palette,
        containerColor = palette.surface
    ) {
        androidx.compose.foundation.layout.Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(iconColor.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.Text(
                    text = icon,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = iconColor
                )
            }

            Column(modifier = Modifier.padding(end = 4.dp)) {
                androidx.compose.material3.Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = palette.textPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                androidx.compose.material3.Text(
                    text = description,
                    fontSize = 15.sp,
                    color = palette.textSecondary,
                    lineHeight = 22.sp
                )
            }
        }
    }
}
