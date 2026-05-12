package com.pashuaahar.ui.screens

import android.graphics.Color as AndroidColor
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
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

@Composable
fun ChartsScreen(navController: androidx.navigation.NavController, sharedViewModel: SharedViewModel) {
    val palette = rememberFarmPalette(sharedViewModel.isDarkMode)
    val result = sharedViewModel.feedResult

    FarmScrollPage(
        palette = palette,
        showTopBar = true,
        title = "Feed Analytics",
        onBackClick = { navController.popBackStack() }
    ) {
        Card(shape = farmCardShape(), colors = CardDefaults.cardColors(containerColor = palette.surface), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Feed analytics", color = palette.textPrimary, fontWeight = FontWeight.Black, fontSize = 29.sp)
                Text(
                    "These MPAndroidChart visuals update from your current calculator inputs without any internet connection.",
                    color = palette.textSecondary,
                    lineHeight = 22.sp
                )
            }
        }

        if (result == null) {
            Card(shape = farmCardShape(), colors = CardDefaults.cardColors(containerColor = palette.surfaceStrong), modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Generate a feed plan first to view the savings trend and ingredient cost chart.",
                    modifier = Modifier.padding(20.dp),
                    color = palette.textSecondary
                )
            }
            return@FarmScrollPage
        }

        SavingsLineChartCard(result = result, palette = palette)
        IngredientBarChartCard(result = result, palette = palette)
    }
}

@Composable
private fun SavingsLineChartCard(result: FeedResult, palette: FarmPalette) {
    Card(shape = farmCardShape(), colors = CardDefaults.cardColors(containerColor = palette.surface), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Savings over time", color = palette.textPrimary, fontWeight = FontWeight.Black, fontSize = 22.sp)
            Text("Daily, weekly, monthly, and yearly projections from the same homemade recipe.", color = palette.textSecondary)
            AndroidView(
                factory = { context ->
                    LineChart(context).apply {
                        layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                        description.isEnabled = false
                        legend.isEnabled = false
                        setTouchEnabled(false)
                        setNoDataText("No savings data")
                        setBackgroundColor(AndroidColor.TRANSPARENT)
                        axisRight.isEnabled = false
                        axisLeft.apply {
                            setDrawAxisLine(false)
                            gridColor = AndroidColor.parseColor("#D6E4D1")
                            textColor = AndroidColor.parseColor("#667085")
                        }
                        xAxis.apply {
                            position = XAxis.XAxisPosition.BOTTOM
                            setDrawAxisLine(false)
                            setDrawGridLines(false)
                            textColor = AndroidColor.parseColor("#667085")
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
                        color = AndroidColor.parseColor("#2F6C44")
                        lineWidth = 3f
                        setDrawCircles(true)
                        circleRadius = 5f
                        circleHoleRadius = 2.5f
                        setCircleColor(AndroidColor.parseColor("#D16A2F"))
                        valueTextSize = 11f
                        valueTextColor = AndroidColor.parseColor("#101828")
                        setDrawFilled(true)
                        fillColor = AndroidColor.parseColor("#D7EBCF")
                        mode = LineDataSet.Mode.CUBIC_BEZIER
                        valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String = "Rs ${"%.0f".format(value)}"
                        }
                    }
                    chart.data = LineData(dataSet)
                    chart.animateX(700)
                    chart.invalidate()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            )
        }
    }
}

@Composable
private fun IngredientBarChartCard(result: FeedResult, palette: FarmPalette) {
    Card(shape = farmCardShape(), colors = CardDefaults.cardColors(containerColor = palette.surface), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Ingredient cost breakdown", color = palette.textPrimary, fontWeight = FontWeight.Black, fontSize = 22.sp)
            Text("A live bar chart of what each ingredient contributes to total homemade cost.", color = palette.textSecondary)
            AndroidView(
                factory = { context ->
                    BarChart(context).apply {
                        layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                        description.isEnabled = false
                        legend.isEnabled = false
                        setTouchEnabled(false)
                        setNoDataText("No ingredient data")
                        setBackgroundColor(AndroidColor.TRANSPARENT)
                        axisRight.isEnabled = false
                        axisLeft.apply {
                            setDrawAxisLine(false)
                            gridColor = AndroidColor.parseColor("#D6E4D1")
                            textColor = AndroidColor.parseColor("#667085")
                            axisMinimum = 0f
                        }
                        xAxis.apply {
                            position = XAxis.XAxisPosition.BOTTOM
                            setDrawAxisLine(false)
                            setDrawGridLines(false)
                            textColor = AndroidColor.parseColor("#667085")
                            granularity = 1f
                            valueFormatter = IndexAxisValueFormatter(listOf("Maize", "Cake", "Bran"))
                        }
                    }
                },
                update = { chart ->
                    val entries = listOf(
                        BarEntry(0f, result.maizeCostRupees.toFloat()),
                        BarEntry(1f, result.cottonseedCostRupees.toFloat()),
                        BarEntry(2f, result.wheatBranCostRupees.toFloat())
                    )
                    val dataSet = BarDataSet(entries, "Ingredients").apply {
                        colors = listOf(
                            AndroidColor.parseColor("#D4A72C"),
                            AndroidColor.parseColor("#D16A2F"),
                            AndroidColor.parseColor("#7A5C3A")
                        )
                        valueTextSize = 12f
                        valueTextColor = AndroidColor.parseColor("#101828")
                        valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String = "Rs ${"%.0f".format(value)}"
                        }
                    }
                    chart.data = BarData(dataSet).apply { barWidth = 0.45f }
                    chart.animateY(700)
                    chart.invalidate()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            )
        }
    }
}
