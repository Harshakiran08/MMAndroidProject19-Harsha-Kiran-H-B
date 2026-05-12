package com.pashuaahar.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pashuaahar.ui.SharedViewModel
import java.util.Locale

@Composable
fun PlanSummaryCard(
    sharedViewModel: SharedViewModel,
    palette: FarmPalette,
    modifier: Modifier = Modifier
) {
    val result = sharedViewModel.feedResult ?: return

    PressableCard(
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = 420.dp),
        palette = palette,
        containerColor = palette.surface
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Plan Summary", color = palette.textPrimary, fontWeight = FontWeight.Black, fontSize = 20.sp)
            Text(
                "${result.breed} • ${formatPlanNumber(result.weightKg)} kg • ${formatPlanNumber(result.milkYieldLitres)} L/day",
                color = palette.textSecondary,
                lineHeight = 20.sp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SummaryValueTile("Daily cost", "Rs ${formatPlanNumber(result.homemadeCostRupees)}", palette, Modifier.weight(1f))
                SummaryValueTile("Savings", sharedViewModel.savedAmountText(result.dailySavings), palette, Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun ResultActionsCard(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    palette: FarmPalette,
    modifier: Modifier = Modifier,
    primaryActionLabel: String? = null,
    onPrimaryAction: (() -> Unit)? = null
) {
    val context = LocalContext.current

    PressableCard(
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = 420.dp),
        palette = palette,
        containerColor = palette.surface
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Save • Share", color = palette.textPrimary, fontWeight = FontWeight.Black, fontSize = 20.sp)
            Text(
                "Keep the plan, send it, or jump back to edit the inputs.",
                color = palette.textSecondary,
                lineHeight = 20.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        sharedViewModel.toggleFavoriteCurrentRecipe()
                        showFarmToast(context, "Plan saved.", palette)
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = palette.accentStrong, contentColor = Color.White)
                ) {
                    Text("Save", fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, sharedViewModel.generateWhatsAppRecipeText())
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share feed plan"))
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = palette.accent, contentColor = Color.White)
                ) {
                    Text("Share", fontWeight = FontWeight.Bold)
                }
            }

            Button(
                onClick = {
                    sharedViewModel.goToCalculatorStep(0)
                    navController.navigate("input")
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = palette.surfaceStrong, contentColor = palette.textPrimary)
            ) {
                Text("Edit Inputs", fontWeight = FontWeight.Bold)
            }

            if (primaryActionLabel != null && onPrimaryAction != null) {
                Button(
                    onClick = onPrimaryAction,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = palette.accentStrong, contentColor = Color.White)
                ) {
                    Text(primaryActionLabel, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun SummaryValueTile(title: String, value: String, palette: FarmPalette, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = palette.surfaceStrong
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(title, color = palette.textSecondary, fontSize = 12.sp)
            Text(value, color = palette.textPrimary, fontWeight = FontWeight.Black, fontSize = 15.sp)
        }
    }
}

private fun formatPlanNumber(value: Double): String {
    return if (value % 1.0 == 0.0) {
        value.toInt().toString()
    } else {
        String.format(Locale.US, "%.1f", value)
    }
}
