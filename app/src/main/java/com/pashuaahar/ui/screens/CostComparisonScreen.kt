package com.pashuaahar.ui.screens

import android.graphics.Color as AndroidColor
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.pashuaahar.model.FeedResult
import com.pashuaahar.ui.SharedViewModel

private const val FARM_GRID = "#4D6F8F2F"
private const val FARM_TEXT = "#294B1E"
private const val FARM_SOFT_FILL = "#DCE9AE"
private const val FARM_ACCENT = "#6F8F2F"
private const val FARM_ACCENT_STRONG = "#3F6B1B"
private const val FARM_HIGHLIGHT = "#D9B43B"
private const val FARM_MUTED = "#A4BF68"

@Composable
fun CostComparisonScreen(navController: NavController, sharedViewModel: SharedViewModel) {
    val palette = rememberFarmPalette(sharedViewModel.isDarkMode)
    val result = sharedViewModel.feedResult

    FarmScrollPage(
        palette = palette,
        showTopBar = true,
        title = "Cost Comparison",
        onBackClick = { navController.popBackStack() },
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 12.dp),
        verticalSpacing = 12.dp
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
                Text("Cost Comparison", color = Color.White, fontWeight = FontWeight.Black, fontSize = 28.sp)
                Text("Compare homemade mix against market feed with savings and charts.", color = Color.White.copy(alpha = 0.9f), lineHeight = 21.sp)
            }
        }

        if (result == null) {
            PressableCard(modifier = Modifier.fillMaxWidth(), palette = palette, containerColor = palette.surface) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("No comparison data yet.", color = palette.textPrimary, fontWeight = FontWeight.Black, fontSize = 22.sp)
                    Text("Generate the recipe first to compare homemade and market cost.", color = palette.textSecondary)
                }
            }
            return@FarmScrollPage
        }

        PlanSummaryCard(sharedViewModel = sharedViewModel, palette = palette)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            CostSummaryCard(Modifier.weight(1f), "Homemade", "Rs ${result.homemadeCostRupees}", palette.accentSoft, palette.accentStrong, palette)
            CostSummaryCard(Modifier.weight(1f), "Market", "Rs ${result.marketCostRupees}", palette.surfaceMuted, palette.accent, palette)
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            CostSummaryCard(Modifier.weight(1f), "Save/day", "Rs ${result.dailySavings}", palette.accentSoft, palette.highlight, palette)
            CostSummaryCard(Modifier.weight(1f), "Price/kg", "Rs ${result.homemadeCostPerKg}", palette.surfaceMuted, palette.textSecondary, palette)
        }

        PressableCard(modifier = Modifier.fillMaxWidth(), palette = palette, containerColor = palette.surface) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Comparison Summary", color = palette.textPrimary, fontWeight = FontWeight.Black, fontSize = 20.sp)
                CostPair("Maize", result.maizeCostRupees, palette)
                CostPair("Cottonseed cake", result.cottonseedCostRupees, palette)
                CostPair("Bran", result.wheatBranCostRupees, palette)
                CostPair("Homemade total", result.homemadeCostRupees, palette)
                CostPair("Market estimate", result.marketCostRupees, palette)
            }
        }

        CostChartsCard(result = result, palette = palette)
        ResultActionsCard(
            navController = navController,
            sharedViewModel = sharedViewModel,
            palette = palette,
            primaryActionLabel = "Continue to Video Tips",
            onPrimaryAction = { navController.navigate("tips") }
        )
    }
}

@Composable
private fun CostSummaryCard(modifier: Modifier, title: String, value: String, background: Color, valueColor: Color, palette: FarmPalette) {
    Surface(modifier = modifier, shape = RoundedCornerShape(20.dp), color = background) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(8.dp).background(valueColor, CircleShape))
                Text(title, color = palette.textSecondary, fontSize = 13.sp)
            }
            Text(value, color = valueColor, fontWeight = FontWeight.Black, fontSize = 18.sp)
        }
    }
}

@Composable
private fun CostPair(label: String, value: Double, palette: FarmPalette) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = palette.textPrimary, fontWeight = FontWeight.Medium)
        Text("Rs ${"%.2f".format(value)}", color = palette.accent, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun CostChartsCard(result: FeedResult, palette: FarmPalette) {
    PressableCard(modifier = Modifier.fillMaxWidth(), palette = palette, containerColor = palette.surfaceStrong) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text("Charts", color = palette.textPrimary, fontWeight = FontWeight.Black, fontSize = 20.sp)
            Text("Savings trend and ingredient cost comparison.", color = palette.textSecondary, fontSize = 13.sp)

            Surface(shape = RoundedCornerShape(20.dp), color = palette.surface) {
                AndroidView(
                    factory = { context ->
                        LineChart(context).apply {
                            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                            description.isEnabled = false
                            legend.isEnabled = false
                            setTouchEnabled(false)
                            setBackgroundColor(AndroidColor.TRANSPARENT)
                            axisRight.isEnabled = false
                            axisLeft.apply {
                                setDrawAxisLine(false)
                                gridColor = AndroidColor.parseColor(FARM_GRID)
                                textColor = AndroidColor.parseColor(FARM_TEXT)
                            }
                            xAxis.apply {
                                position = XAxis.XAxisPosition.BOTTOM
                                setDrawAxisLine(false)
                                setDrawGridLines(false)
                                textColor = AndroidColor.parseColor(FARM_TEXT)
                                granularity = 1f
                                valueFormatter = IndexAxisValueFormatter(listOf("Day", "Week", "Month", "Year"))
                            }
                        }
                    },
                    update = { chart ->
                        val entries = listOf(
                            Entry(0f, result.dailySavings.toFloat()),
                            Entry(1f, result.weeklySavings.toFloat()),
                            Entry(2f, result.monthlySavings.toFloat()),
                            Entry(3f, result.yearlySavings.toFloat())
                        )
                        val dataSet = LineDataSet(entries, "Savings").apply {
                            color = AndroidColor.parseColor(FARM_ACCENT_STRONG)
                            lineWidth = 3f
                            setDrawCircles(true)
                            setCircleColor(AndroidColor.parseColor(FARM_HIGHLIGHT))
                            setDrawFilled(true)
                            fillColor = AndroidColor.parseColor(FARM_SOFT_FILL)
                            valueTextColor = AndroidColor.parseColor(FARM_TEXT)
                            valueTextSize = 10f
                            mode = LineDataSet.Mode.CUBIC_BEZIER
                            valueFormatter = object : ValueFormatter() {
                                override fun getFormattedValue(value: Float): String = "Rs ${"%.0f".format(value)}"
                            }
                        }
                        chart.data = LineData(dataSet)
                        chart.invalidate()
                    },
                    modifier = Modifier.fillMaxWidth().height(220.dp).padding(10.dp)
                )
            }

            Surface(shape = RoundedCornerShape(20.dp), color = palette.surface) {
                AndroidView(
                    factory = { context ->
                        BarChart(context).apply {
                            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                            description.isEnabled = false
                            legend.isEnabled = false
                            setTouchEnabled(false)
                            setBackgroundColor(AndroidColor.TRANSPARENT)
                            axisRight.isEnabled = false
                            axisLeft.apply {
                                setDrawAxisLine(false)
                                gridColor = AndroidColor.parseColor(FARM_GRID)
                                textColor = AndroidColor.parseColor(FARM_TEXT)
                                axisMinimum = 0f
                            }
                            xAxis.apply {
                                position = XAxis.XAxisPosition.BOTTOM
                                setDrawAxisLine(false)
                                setDrawGridLines(false)
                                textColor = AndroidColor.parseColor(FARM_TEXT)
                                granularity = 1f
                                valueFormatter = IndexAxisValueFormatter(listOf("Maize", "Cake", "Bran", "Home", "Market"))
                            }
                        }
                    },
                    update = { chart ->
                        val entries = listOf(
                            BarEntry(0f, result.maizeCostRupees.toFloat()),
                            BarEntry(1f, result.cottonseedCostRupees.toFloat()),
                            BarEntry(2f, result.wheatBranCostRupees.toFloat()),
                            BarEntry(3f, result.homemadeCostRupees.toFloat()),
                            BarEntry(4f, result.marketCostRupees.toFloat())
                        )
                        val dataSet = BarDataSet(entries, "Costs").apply {
                            colors = listOf(
                                AndroidColor.parseColor(FARM_HIGHLIGHT),
                                AndroidColor.parseColor(FARM_ACCENT),
                                AndroidColor.parseColor(FARM_MUTED),
                                AndroidColor.parseColor(FARM_ACCENT_STRONG),
                                AndroidColor.parseColor(FARM_TEXT)
                            )
                            valueTextColor = AndroidColor.parseColor(FARM_TEXT)
                            valueTextSize = 11f
                            valueFormatter = object : ValueFormatter() {
                                override fun getFormattedValue(value: Float): String = "Rs ${"%.0f".format(value)}"
                            }
                        }
                        chart.data = BarData(dataSet).apply { barWidth = 0.45f }
                        chart.invalidate()
                    },
                    modifier = Modifier.fillMaxWidth().height(220.dp).padding(10.dp)
                )
            }
        }
    }
}
