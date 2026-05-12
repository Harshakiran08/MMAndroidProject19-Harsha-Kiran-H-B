package com.pashuaahar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pashuaahar.ui.SharedViewModel
import com.pashuaahar.ui.theme.Strings

@Composable
fun HomeScreen(shellNavController: NavController, rootNavController: NavController, sharedViewModel: SharedViewModel) {
    val isK = sharedViewModel.languageCode == "kn"
    val palette = rememberFarmPalette(sharedViewModel.isDarkMode)
    val displayName = sharedViewModel.currentUsername.ifBlank {
        if (isK) "ರೈತ" else "Farmer"
    }

    FarmScrollPage(
        palette = palette,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalSpacing = 12.dp
    ) {
        LedgerStyleHeroCard(
            palette = palette,
            title = Strings.get("healthy_cattle_milk", isK),
            subtitle = Strings.get("right_feed", isK),
            greeting = if (isK) "ನಮಸ್ಕಾರ $displayName" else "Hello $displayName",
            isKannada = isK
        )

        Text(
            text = Strings.get("how_it_works", isK),
            color = palette.textPrimary,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp
        )

        VerticalFlowList(
            items = listOf(
                FlowItem("1", Strings.get("input_step", isK), Strings.get("input_step_desc", isK), Icons.Default.Calculate, palette.accentStrong),
                FlowItem("2", Strings.get("calculate_step", isK), Strings.get("calculate_step_desc", isK), Icons.Default.Science, palette.accent),
                FlowItem("3", Strings.get("values_step", isK), Strings.get("values_step_desc", isK), Icons.Default.CurrencyRupee, palette.highlight),
                FlowItem("4", Strings.get("tips_step", isK), Strings.get("tips_step_desc", isK), Icons.Default.PlayCircle, palette.textSecondary)
            ),
            palette = palette
        )
    }
}

private data class FlowItem(
    val step: String,
    val title: String,
    val subtitle: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val accent: Color
)

@Composable
private fun LedgerStyleHeroCard(
    palette: FarmPalette,
    title: String,
    subtitle: String,
    greeting: String,
    isKannada: Boolean
) {
    Surface(
        shape = RoundedCornerShape(28.dp),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(palette.accentStrong)
                .padding(horizontal = 20.dp, vertical = 22.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(greeting, color = Color.White.copy(alpha = 0.85f), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Text(title, color = Color.White, fontSize = 27.sp, fontWeight = FontWeight.Black, lineHeight = 32.sp)
            Text(subtitle, color = Color.White.copy(alpha = 0.94f), fontWeight = FontWeight.Bold, fontSize = 14.sp)

            Surface(shape = RoundedCornerShape(18.dp), color = palette.highlight) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("Goal", color = palette.textPrimary.copy(alpha = 0.75f), fontSize = 11.sp)
                    Text("Reduce Cost with a 4-step recipe flow", color = palette.textPrimary, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                }
            }
        }
    }
}

@Composable
private fun VerticalFlowList(items: List<FlowItem>, palette: FarmPalette) {
    items.forEach { item ->
        PressableCard(
            modifier = Modifier.fillMaxWidth(),
            palette = palette,
            containerColor = palette.surfaceStrong
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                androidx.compose.foundation.layout.Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(item.accent.copy(alpha = 0.14f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(item.step, color = item.accent, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                    }
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(item.title, color = palette.accentStrong, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                        Text(item.subtitle, color = palette.textPrimary, fontSize = 12.sp, lineHeight = 17.sp)
                    }
                    Icon(item.icon, contentDescription = item.title, tint = item.accent, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}
