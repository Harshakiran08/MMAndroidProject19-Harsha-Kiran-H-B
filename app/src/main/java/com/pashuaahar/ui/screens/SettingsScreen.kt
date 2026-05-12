package com.pashuaahar.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pashuaahar.ui.SharedViewModel
import com.pashuaahar.ui.theme.Strings

@Composable
fun SettingsScreen(sharedViewModel: SharedViewModel) {
    val palette = rememberFarmPalette(sharedViewModel.isDarkMode)
    val context = LocalContext.current
    val isK = sharedViewModel.languageCode == "kn"
    var maize by remember { mutableStateOf(sharedViewModel.priceMaize.toString()) }
    var cottonseed by remember { mutableStateOf(sharedViewModel.priceCottonseed.toString()) }
    var bran by remember { mutableStateOf(sharedViewModel.priceBran.toString()) }
    var market by remember { mutableStateOf(sharedViewModel.priceMarket.toString()) }

    LaunchedEffect(
        sharedViewModel.priceMaize,
        sharedViewModel.priceCottonseed,
        sharedViewModel.priceBran,
        sharedViewModel.priceMarket
    ) {
        maize = sharedViewModel.priceMaize.toString()
        cottonseed = sharedViewModel.priceCottonseed.toString()
        bran = sharedViewModel.priceBran.toString()
        market = sharedViewModel.priceMarket.toString()
    }

    FarmScrollPage(palette = palette) {
        SettingsBlock(
            title = Strings.get("market_price_set", isK),
            subtitle = Strings.get("market_price_desc", isK),
            icon = { Icon(Icons.Default.Settings, contentDescription = null, tint = palette.accentStrong) },
            palette = palette
        ) {
            PriceInput("Maize price", maize, palette) { maize = it }
            PriceInput("Cottonseed price", cottonseed, palette) { cottonseed = it }
            PriceInput("Wheat bran price", bran, palette) { bran = it }
            PriceInput("Commercial feed", market, palette) { market = it }
            Button(
                onClick = {
                    val maizeValue = maize.toFloatOrNull()
                    val cottonseedValue = cottonseed.toFloatOrNull()
                    val branValue = bran.toFloatOrNull()
                    val marketValue = market.toFloatOrNull()
                    if (listOf(maizeValue, cottonseedValue, branValue, marketValue).any { it == null || it < 0f }) {
                        showFarmToast(context, Strings.get("invalid_location_prices", isK), palette)
                    } else {
                        sharedViewModel.savePrices(
                            maize = maizeValue ?: 25f,
                            cottonseed = cottonseedValue ?: 40f,
                            bran = branValue ?: 20f,
                            market = marketValue ?: 35f
                        )
                        showFarmToast(context, Strings.get("market_saved", isK), palette)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = palette.accentStrong)
            ) {
                Text(Strings.get("save_market_price_set", isK), color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun SettingsBlock(
    title: String,
    subtitle: String,
    icon: @Composable () -> Unit,
    palette: FarmPalette,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = palette.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            androidx.compose.foundation.layout.Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(shape = RoundedCornerShape(16.dp), color = palette.surfaceStrong) {
                    androidx.compose.foundation.layout.Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        icon()
                    }
                }
                Column {
                    Text(title, color = palette.textPrimary, fontWeight = FontWeight.Bold, fontSize = 21.sp)
                    Text(subtitle, color = palette.textSecondary, lineHeight = 20.sp)
                }
            }
            content()
        }
    }
}

@Composable
private fun PriceInput(label: String, value: String, palette: FarmPalette, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        shape = RoundedCornerShape(18.dp),
        colors = farmTextFieldColors(palette)
    )
}
