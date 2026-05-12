package com.pashuaahar.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pashuaahar.model.TipCategory
import com.pashuaahar.model.VeterinaryTip
import com.pashuaahar.ui.SharedViewModel

@Composable
fun TipsScreen(navController: NavController, sharedViewModel: SharedViewModel) {
    val palette = rememberFarmPalette(sharedViewModel.isDarkMode)
    val context = LocalContext.current
    var selectedCategory by rememberSaveable { mutableStateOf<TipCategory?>(null) }
    val tips = sharedViewModel.veterinaryTips.filter { selectedCategory == null || it.category == selectedCategory }

    FarmScrollPage(
        palette = palette,
        showTopBar = true,
        title = "Video Tips",
        onBackClick = { navController.popBackStack() },
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 12.dp),
        verticalSpacing = 14.dp
    ) {
        ModuleHeroCard(
            palette = palette,
            title = "Video Tips",
            subtitle = "Open short guidance videos for nutrition, hygiene, and safe feed storage."
        )

        PressableCard(
            modifier = Modifier.fillMaxWidth(),
            palette = palette,
            containerColor = palette.surface
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Browse by category",
                    color = palette.textPrimary,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp
                )
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        CategoryChip(
                            modifier = Modifier.weight(1f),
                            title = "All",
                            selected = selectedCategory == null,
                            onClick = { selectedCategory = null },
                            palette = palette
                        )
                        CategoryChip(
                            modifier = Modifier.weight(1f),
                            title = "Nutrition",
                            selected = selectedCategory == TipCategory.NUTRITION,
                            onClick = { selectedCategory = TipCategory.NUTRITION },
                            palette = palette
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        CategoryChip(
                            modifier = Modifier.weight(1f),
                            title = "Hygiene",
                            selected = selectedCategory == TipCategory.HYGIENE,
                            onClick = { selectedCategory = TipCategory.HYGIENE },
                            palette = palette
                        )
                        CategoryChip(
                            modifier = Modifier.weight(1f),
                            title = "Storage",
                            selected = selectedCategory == TipCategory.STORAGE,
                            onClick = { selectedCategory = TipCategory.STORAGE },
                            palette = palette
                        )
                    }
                }
            }
        }

        tips.forEachIndexed { index, tip ->
            VeterinaryTipCard(
                tip = tip,
                palette = palette,
                accent = when (index % 3) {
                    0 -> palette.accentStrong
                    1 -> palette.accent
                    else -> palette.highlight
                },
                onPlay = {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(tip.videoUrl)))
                }
            )
        }

        Button(
            onClick = { navController.navigate("final") },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .widthIn(max = 320.dp)
                .height(56.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = palette.accent)
        ) {
            Text("Complete Plan", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 17.sp)
        }
    }
}

@Composable
private fun CategoryChip(
    modifier: Modifier,
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    palette: FarmPalette
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        color = if (selected) palette.accent else palette.surfaceStrong
    ) {
        Box(
            modifier = Modifier.padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                title,
                color = if (selected) Color.White else palette.textPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun VeterinaryTipCard(
    tip: VeterinaryTip,
    palette: FarmPalette,
    accent: Color,
    onPlay: () -> Unit
) {
    Card(
        shape = farmCardShape(),
        colors = CardDefaults.cardColors(containerColor = palette.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(accent.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("TIP", color = accent, fontWeight = FontWeight.Black, fontSize = 10.sp)
                }
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(tip.title, color = palette.textPrimary, fontWeight = FontWeight.Black, fontSize = 16.sp)
                    Text(tip.category.label, color = accent, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }

            Surface(shape = RoundedCornerShape(16.dp), color = palette.surfaceStrong) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = tip.videoLabel,
                        color = accent,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                    Text(
                        text = tip.description,
                        color = palette.textSecondary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }

            Surface(shape = RoundedCornerShape(14.dp), color = accent.copy(alpha = 0.10f)) {
                Text(
                    text = "Tap below to open the video tip in your browser.",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    color = palette.textPrimary,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }

            Button(
                onClick = onPlay,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accent, contentColor = Color.White)
            ) {
                Text("Watch Video Tip", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}
