package com.pashuaahar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.pashuaahar.model.FeedResult
import com.pashuaahar.ui.SharedViewModel

@Composable
fun FeedCalculatorScreen(navController: NavController, sharedViewModel: SharedViewModel) {
    val palette = rememberFarmPalette(sharedViewModel.isDarkMode)
    val result = sharedViewModel.feedResult

    FarmScrollPage(
        palette = palette,
        showTopBar = true,
        title = "Feed Recipe",
        onBackClick = { navController.popBackStack() },
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 12.dp),
        verticalSpacing = 12.dp
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Column(
                modifier = Modifier
                    .background(palette.surfaceMuted)
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text("Feed Recipe", color = palette.textPrimary, fontWeight = FontWeight.Black, fontSize = 26.sp)
                Text(
                    "Compact recipe view with quantities, cost, nutrition, and timing.",
                    color = palette.textSecondary,
                    lineHeight = 20.sp
                )
            }
        }

        if (result == null) {
            Card(shape = farmCardShape(), colors = CardDefaults.cardColors(containerColor = palette.surface), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("No feed result yet.", color = palette.textPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("Open the calculator stepper and generate a plan first.", color = palette.textSecondary)
                    Button(
                        onClick = { navController.navigate("input") },
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = palette.accent)
                    ) {
                        Text("Open stepper", color = Color.White)
                    }
                }
            }
            return@FarmScrollPage
        }

        PlanSummaryCard(sharedViewModel = sharedViewModel, palette = palette)
        SummaryHero(result = result, palette = palette)
        IngredientTableCard(result = result, palette = palette)
        CompactNutritionCard(result = result, palette = palette)
        CompactWhyCard(result = result, palette = palette)
        TimetableCard(result = result, palette = palette)
        ResultActionsCard(
            navController = navController,
            sharedViewModel = sharedViewModel,
            palette = palette,
            primaryActionLabel = "Next: Cost Comparison",
            onPrimaryAction = { navController.navigate("cost_comparison") }
        )
    }
}

@Composable
private fun SummaryHero(result: FeedResult, palette: FarmPalette) {
    PressableCard(
        modifier = Modifier.fillMaxWidth(),
        palette = palette,
        containerColor = palette.surface
    ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("${result.breed} cow feeding plan", color = palette.textPrimary, fontWeight = FontWeight.Black, fontSize = 22.sp)
            Text(
                "${result.weightKg} kg • ${result.ageYears} years • ${result.milkYieldLitres} L/day",
                color = palette.textSecondary,
                fontSize = 13.sp
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                MiniResultTile(modifier = Modifier.weight(1f), title = "Home", value = "Rs ${result.homemadeCostRupees}", palette = palette, background = palette.accentSoft, valueColor = palette.accentStrong)
                MiniResultTile(modifier = Modifier.weight(1f), title = "Save/day", value = "Rs ${result.dailySavings}", palette = palette, background = palette.surfaceMuted, valueColor = palette.highlight)
                MiniResultTile(modifier = Modifier.weight(1f), title = "Feed", value = "${result.maizeKg + result.cottonseedCakeKg + result.wheatBranKg} kg", palette = palette, background = palette.accentSoft, valueColor = palette.textSecondary)
            }
        }
    }
}

@Composable
private fun MiniResultTile(modifier: Modifier, title: String, value: String, palette: FarmPalette, background: Color = palette.surfaceStrong, valueColor: Color = palette.textPrimary) {
    Surface(modifier = modifier, shape = RoundedCornerShape(18.dp), color = background) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, color = palette.textSecondary, fontSize = 12.sp)
            Text(value, color = valueColor, fontWeight = FontWeight.Black, fontSize = 16.sp)
        }
    }
}

@Composable
private fun IngredientTableCard(result: FeedResult, palette: FarmPalette) {
    PressableCard(
        modifier = Modifier.fillMaxWidth(),
        palette = palette,
        containerColor = palette.surface
    ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Recipe Table", color = palette.textPrimary, fontWeight = FontWeight.Black, fontSize = 20.sp)
            Surface(shape = RoundedCornerShape(18.dp), color = palette.surfaceStrong) {
                Column {
                    RecipeHeader(palette)
                    RecipeDivider(palette)
                    result.feedIngredients.forEachIndexed { index, item ->
                        RecipeRow(
                            ingredient = item.name,
                            quantity = "${item.quantityKg} kg",
                            cost = "Rs ${item.costRupees}",
                            palette = palette
                        )
                        if (index != result.feedIngredients.lastIndex) {
                            RecipeDivider(palette)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RecipeHeader(palette: FarmPalette) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(palette.accentSoft)
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        TableText("Ingredient", Modifier.weight(1.3f), palette.accentStrong, true)
        TableText("Qty", Modifier.weight(0.8f), palette.accentStrong, true)
        TableText("Cost", Modifier.weight(0.8f), palette.accentStrong, true)
    }
}

@Composable
private fun RecipeRow(ingredient: String, quantity: String, cost: String, palette: FarmPalette) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TableText(ingredient, Modifier.weight(1.3f), palette.textPrimary, true)
        TableText(quantity, Modifier.weight(0.8f), palette.textSecondary, false)
        TableText(cost, Modifier.weight(0.8f), palette.accent, true)
    }
}

@Composable
private fun TableText(text: String, modifier: Modifier, color: Color, bold: Boolean) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontWeight = if (bold) FontWeight.Bold else FontWeight.Medium,
        fontSize = 13.sp
    )
}

@Composable
private fun CompactNutritionCard(result: FeedResult, palette: FarmPalette) {
    PressableCard(
        modifier = Modifier.fillMaxWidth(),
        palette = palette,
        containerColor = palette.surfaceStrong
    ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Nutrition Overview", color = palette.textPrimary, fontWeight = FontWeight.Black, fontSize = 20.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                MiniResultTile(Modifier.weight(1f), "Protein", "${result.proteinPercent}%", palette, palette.surface, palette.accentStrong)
                MiniResultTile(Modifier.weight(1f), "Energy", "${result.energyValueMcal} Mcal", palette, palette.surface, palette.textSecondary)
                MiniResultTile(Modifier.weight(1f), "Fiber", "${result.fiberPercent}%", palette, palette.surface, palette.highlight)
            }
        }
    }
}

@Composable
private fun CompactWhyCard(result: FeedResult, palette: FarmPalette) {
    PressableCard(
        modifier = Modifier.fillMaxWidth(),
        palette = palette,
        containerColor = palette.surface
    ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Why this plan changed", color = palette.textPrimary, fontWeight = FontWeight.Black, fontSize = 20.sp)
            result.recommendations.take(2).forEach { note ->
                Surface(shape = RoundedCornerShape(16.dp), color = palette.surfaceStrong) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(note.title, color = palette.textPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(note.body, color = palette.textSecondary, fontSize = 12.sp, lineHeight = 17.sp, maxLines = 2)
                    }
                }
            }
        }
    }
}

@Composable
private fun TimetableCard(result: FeedResult, palette: FarmPalette) {
    val totalFeed = result.maizeKg + result.cottonseedCakeKg + result.wheatBranKg
    val morningFeed = totalFeed * 0.45
    val eveningFeed = totalFeed * 0.55

    PressableCard(
        modifier = Modifier.fillMaxWidth(),
        palette = palette,
        containerColor = palette.surfaceStrong
    ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Daily Feeding Timetable", color = palette.textPrimary, fontWeight = FontWeight.Black, fontSize = 20.sp)
            Surface(shape = RoundedCornerShape(18.dp), color = palette.surface) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(palette.accentSoft)
                            .padding(horizontal = 14.dp, vertical = 12.dp)
                    ) {
                        TableText("Time", Modifier.weight(1f), palette.accentStrong, true)
                        TableText("Feed", Modifier.weight(0.9f), palette.accentStrong, true)
                        TableText("Note", Modifier.weight(1.4f), palette.accentStrong, true)
                    }
                    RecipeDivider(palette)
                    TimeRow("Morning", "${"%.2f".format(morningFeed)} kg", "During milking with water", palette)
                    RecipeDivider(palette)
                    TimeRow("Evening", "${"%.2f".format(eveningFeed)} kg", "With dry fodder support", palette)
                }
            }
        }
    }
}

@Composable
private fun TimeRow(time: String, feed: String, note: String, palette: FarmPalette) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        TableText(time, Modifier.weight(1f), palette.textPrimary, true)
        TableText(feed, Modifier.weight(0.9f), palette.accent, true)
        TableText(note, Modifier.weight(1.4f), palette.textSecondary, false)
    }
}

@Composable
private fun RecipeDivider(palette: FarmPalette) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(palette.border)
    )
}
