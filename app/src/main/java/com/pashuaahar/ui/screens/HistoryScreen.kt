package com.pashuaahar.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pashuaahar.data.db.FeedHistory
import com.pashuaahar.ui.SharedViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.pashuaahar.ui.theme.Strings

@Composable
fun HistoryScreen(navController: androidx.navigation.NavController, sharedViewModel: SharedViewModel) {
    val historyList by sharedViewModel.allHistory.collectAsState(initial = emptyList())
    val palette = rememberFarmPalette(sharedViewModel.isDarkMode)
    val isK = sharedViewModel.languageCode == "kn"

    FarmBackground(palette = palette) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            Column(modifier = Modifier.padding(top = 18.dp, start = 18.dp, end = 18.dp)) {
                Text(
                    text = Strings.get("feed_history", isK),
                    color = palette.textPrimary,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = Strings.get("review_past", isK),
                    color = palette.textSecondary,
                    fontSize = 16.sp
                )
            }

            Column(modifier = Modifier.fillMaxSize().padding(horizontal = 18.dp, vertical = 8.dp)) {
                if (historyList.isEmpty()) {
                    Text(Strings.get("no_history_found", isK), color = palette.textSecondary)
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxSize()) {
                        items(historyList) { entry ->
                            HistoryCard(entry, sharedViewModel, palette)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryCard(entry: FeedHistory, sharedViewModel: SharedViewModel, palette: FarmPalette) {
    val isK = sharedViewModel.languageCode == "kn"
    val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    val dateStr = sdf.format(Date(entry.date))

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = palette.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(entry.cowName, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = palette.accentStrong)
                Text(dateStr, fontSize = 13.sp, color = palette.textSecondary)
            }
            Text("${entry.breed} | ${entry.animalType} | ${entry.location}", fontSize = 14.sp, color = palette.textPrimary)
            Text("${Strings.get("weight", isK)} ${entry.weight} ${Strings.get("kg", isK)} | ${Strings.get("milk", isK)} ${entry.milkYield} ${Strings.get("l_per_day", isK)}", fontSize = 14.sp, color = palette.textPrimary)
            Text("${Strings.get("mix", isK)}: ${"%.1f".format(entry.maizeKg)} ${Strings.get("kg_maize", isK)}, ${"%.1f".format(entry.cottonseedKg)} ${Strings.get("kg_cottonseed", isK)}, ${"%.1f".format(entry.branKg)} ${Strings.get("kg_bran", isK)}", fontSize = 14.sp, color = palette.textPrimary)
            Spacer(modifier = Modifier.height(2.dp))
            Text(sharedViewModel.savedAmountText(entry.dailySavings.takeIf { it != 0.0 } ?: entry.savingsRupees), fontWeight = FontWeight.Bold, color = palette.accent)
        }
    }
}
