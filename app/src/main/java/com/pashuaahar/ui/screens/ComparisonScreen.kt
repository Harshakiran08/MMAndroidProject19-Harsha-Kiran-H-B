package com.pashuaahar.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pashuaahar.R
import com.pashuaahar.ui.SharedViewModel

@Composable
fun ComparisonScreen(navController: NavController, sharedViewModel: SharedViewModel, showBackButton: Boolean = true) {
    val palette = rememberFarmPalette(sharedViewModel.isDarkMode)
    val result = sharedViewModel.feedResult

    FarmScrollPage(
        palette = palette,
        showTopBar = showBackButton,
        title = "Values",
        onBackClick = { navController.popBackStack() },
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 12.dp),
        verticalSpacing = 14.dp
    ) {
        PressableCard(
            modifier = Modifier.fillMaxWidth(),
            palette = palette,
            containerColor = Color.Transparent
        ) {
            Column(
                modifier = Modifier
                    .background(palette.accentStrong)
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Values", color = Color.White, fontWeight = FontWeight.Black, fontSize = 28.sp)
                Text(
                    "Check current feed prices, breed notes, and the latest savings summary.",
                    color = Color.White.copy(alpha = 0.9f),
                    lineHeight = 21.sp
                )
            }
        }

        PressableCard(
            modifier = Modifier.fillMaxWidth(),
            palette = palette,
            containerColor = palette.surface
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Current Local Prices", color = palette.textPrimary, fontWeight = FontWeight.Black, fontSize = 22.sp)
                CostRow("Maize per kg", sharedViewModel.priceMaize.toDouble(), palette)
                CostRow("Cottonseed cake per kg", sharedViewModel.priceCottonseed.toDouble(), palette)
                CostRow("Wheat bran per kg", sharedViewModel.priceBran.toDouble(), palette)
                CostRow("Market feed per kg", sharedViewModel.priceMarket.toDouble(), palette)
            }
        }

        PressableCard(
            modifier = Modifier.fillMaxWidth(),
            palette = palette,
            containerColor = palette.surfaceStrong
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Cattle Breeds Information", color = palette.textPrimary, fontWeight = FontWeight.Black, fontSize = 22.sp)
                ImageBreedCard(
                    title = "Jersey",
                    description = "High fat content milk, adaptable to various climates.",
                    imageRes = R.drawable.jersey_cow,
                    palette = palette
                )
                ImageBreedCard(
                    title = "HF (Holstein Friesian)",
                    description = "High milk output profile, requires cooler environments.",
                    imageRes = R.drawable.hf_cow,
                    palette = palette
                )
                ImageBreedCard(
                    title = "Desi",
                    description = "Local breed, efficient feeding and disease resistant.",
                    imageRes = R.drawable.desi_cow,
                    palette = palette
                )
            }
        }

    }
}

@Composable
private fun CostRow(label: String, value: Double, palette: FarmPalette) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = palette.textPrimary, fontWeight = FontWeight.Medium)
        Text("Rs ${"%.2f".format(value)}", color = palette.accent, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ImageBreedCard(title: String, description: String, imageRes: Int, palette: FarmPalette) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = palette.surface),
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(66.dp)
                    .clip(RoundedCornerShape(14.dp))
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = palette.textPrimary)
                Text(text = description, fontSize = 13.sp, color = palette.textSecondary, lineHeight = 19.sp)
            }
        }
    }
}
