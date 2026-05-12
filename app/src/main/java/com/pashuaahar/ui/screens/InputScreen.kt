package com.pashuaahar.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pashuaahar.model.AnimalType
import com.pashuaahar.model.FarmerGoal
import com.pashuaahar.ui.SharedViewModel
import java.util.Locale

@Composable
fun InputScreen(navController: NavController, sharedViewModel: SharedViewModel) {
    val palette = rememberFarmPalette(sharedViewModel.isDarkMode)

    var breed by rememberSaveable { mutableStateOf(sharedViewModel.selectedBreed) }
    var animalName by rememberSaveable { mutableStateOf(sharedViewModel.cowName) }
    var weightText by rememberSaveable { mutableStateOf(sharedViewModel.weight.toInputText()) }
    var ageText by rememberSaveable { mutableStateOf(sharedViewModel.ageYears.toInputText(defaultWhenZero = "4")) }
    var milkYieldText by rememberSaveable { mutableStateOf(sharedViewModel.milkYield.toInputText()) }

    val step = sharedViewModel.calculatorStep
    val weightValue = weightText.toDoubleOrNull() ?: 0.0
    val ageValue = ageText.toDoubleOrNull() ?: 4.0
    val milkYieldValue = milkYieldText.toDoubleOrNull() ?: 0.0
    val stepTitles = listOf("Breed", "Weight", "Milk")
    val validationMessage = when (step) {
        0 -> if (breed.isBlank()) "Select a breed to continue." else ""
        1 -> when {
            weightText.isBlank() -> "Enter the animal weight."
            weightText.toDoubleOrNull() == null -> "Weight must be a valid number."
            weightValue <= 0 -> "Weight must be greater than 0."
            ageText.isBlank() -> "Enter the animal age."
            ageText.toDoubleOrNull() == null -> "Age must be a valid number."
            ageValue <= 0 -> "Age must be greater than 0."
            else -> ""
        }
        2 -> when {
            milkYieldText.isBlank() -> "Enter milk yield."
            milkYieldText.toDoubleOrNull() == null -> "Milk yield must be a valid number."
            milkYieldValue < 0 -> "Milk yield cannot be negative."
            else -> ""
        }
        else -> ""
    }

    val canGoNext = when (step) {
        0 -> breed.isNotBlank()
        1 -> validationMessage.isBlank()
        2 -> validationMessage.isBlank()
        else -> true
    }

    LaunchedEffect(breed, weightValue, ageValue, milkYieldValue, step) {
        sharedViewModel.cowName = animalName
        sharedViewModel.syncCalculatorInputs(
            breed = breed,
            weight = weightValue,
            ageYears = ageValue,
            milkYield = milkYieldValue,
            maizePrice = sharedViewModel.priceMaize,
            cottonseedPrice = sharedViewModel.priceCottonseed,
            branPrice = sharedViewModel.priceBran
        )
        if (step == 3 && weightValue > 0) {
            sharedViewModel.buildSmartFeedPlan(addToHistory = false)
        }
    }

    FarmScrollPage(
        palette = palette,
        showTopBar = true,
        title = "Enter Details",
        onBackClick = { navController.popBackStack() }
    ) {
        ModuleHeroCard(
            palette = palette,
            title = "Feed Calculator",
            subtitle = "Complete the steps to generate your full recipe and cost comparison."
        )

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "Step ${step + 1} of 3 • ${stepTitles[step.coerceIn(0, stepTitles.lastIndex)]}",
                color = palette.textSecondary,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
            LinearProgressIndicator(
                progress = (step + 1) / 3f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = palette.accent,
                trackColor = palette.surfaceMuted
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(3) { index ->
                        Box(
                            modifier = Modifier
                                .size(if (index == step) 12.dp else 8.dp)
                                .background(
                                    if (index == step) palette.accent else if (index < step) palette.highlight else palette.border,
                                    CircleShape
                                )
                        )
                    }
                }
            }
        }

        AnimatedContent(
            targetState = step,
            transitionSpec = {
                (fadeIn() + scaleIn(initialScale = 0.95f)).togetherWith(fadeOut() + scaleOut(targetScale = 0.95f))
            },
            label = "stepper"
        ) { currentStep ->
            when (currentStep) {
                0 -> StepSurface(palette = palette) {
                    BreedStepContent(
                        animalName = animalName,
                        onAnimalNameChange = { animalName = it },
                        selectedBreed = breed,
                        onBreedSelected = { breed = it },
                        palette = palette
                    )
                }

                1 -> StepSurface(palette = palette) {
                    WeightStepContent(
                        weightText = weightText,
                        onWeightChange = { weightText = it },
                        ageText = ageText,
                        onAgeChange = { ageText = it },
                        palette = palette,
                        validationMessage = validationMessage
                    )
                }

                2 -> StepSurface(palette = palette) {
                    MilkStepContent(
                        milkYieldText = milkYieldText,
                        onMilkYieldChange = { milkYieldText = it },
                        palette = palette,
                        validationMessage = validationMessage
                    )
                }

                else -> StepSurface(palette = palette) {
                    MilkStepContent(
                        milkYieldText = milkYieldText,
                        onMilkYieldChange = { milkYieldText = it },
                        palette = palette,
                        validationMessage = validationMessage
                    )
                }
            }
        }

        InputProgressSummary(
            breed = breed,
            weightText = weightText,
            milkYieldText = milkYieldText,
            palette = palette
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    if (step > 0) {
                        sharedViewModel.goToCalculatorStep(step - 1)
                    } else {
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = palette.textPrimary,
                    disabledContainerColor = Color.White,
                    disabledContentColor = palette.textPrimary
                )
            ) {
                Text("Back", fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = {
                    when (step) {
                        0 -> sharedViewModel.goToCalculatorStep(1)
                        1 -> sharedViewModel.goToCalculatorStep(2)
                        else -> {
                            sharedViewModel.cowName = animalName
                            sharedViewModel.selectedAnimalType = AnimalType.COW
                            sharedViewModel.selectedGoal = FarmerGoal.REDUCE_COST
                            sharedViewModel.buildSmartFeedPlan(addToHistory = true)
                            navController.navigate("calculator")
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                enabled = canGoNext,
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = palette.accent, contentColor = Color.White)
            ) {
                Text(if (step == 2) "Full Recipe" else "Next", fontWeight = FontWeight.Black)
            }
        }
    }
}

@Composable
private fun StepSurface(palette: FarmPalette, content: @Composable ColumnScope.() -> Unit) {
    PressableCard(
        modifier = Modifier.fillMaxWidth(),
        palette = palette,
        containerColor = palette.surface
    ) {
        Column(modifier = Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(18.dp), content = content)
    }
}

@Composable
private fun BreedStepContent(
    animalName: String,
    onAnimalNameChange: (String) -> Unit,
    selectedBreed: String,
    onBreedSelected: (String) -> Unit,
    palette: FarmPalette
) {
    Text("Choose breed", color = palette.textPrimary, fontWeight = FontWeight.Black, fontSize = 26.sp)
    Text("Tap the breed card to start the feed journey.", color = palette.textSecondary)
    OutlinedTextField(
        value = animalName,
        onValueChange = onAnimalNameChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Cow name (optional)") },
        shape = RoundedCornerShape(18.dp),
        colors = farmTextFieldColors(palette)
    )
    listOf("Jersey", "HF", "Desi").forEachIndexed { index, item ->
        val imageRes = when (item) {
            "Jersey" -> com.pashuaahar.R.drawable.jersey_cow
            "HF" -> com.pashuaahar.R.drawable.hf_cow
            else -> com.pashuaahar.R.drawable.desi_cow
        }
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onBreedSelected(item) },
            shape = RoundedCornerShape(22.dp),
            color = if (selectedBreed == item) palette.accentSoft else palette.surfaceStrong
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = item,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )
                Column {
                    Text(item, color = palette.textPrimary, fontWeight = FontWeight.Black, fontSize = 18.sp)
                    Text(
                        when (item) {
                            "HF" -> "High milk output profile"
                            "Desi" -> "Local breed, efficient feeding"
                            else -> "Balanced milk and cost"
                        },
                        color = palette.textSecondary,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun WeightStepContent(
    weightText: String,
    onWeightChange: (String) -> Unit,
    ageText: String,
    onAgeChange: (String) -> Unit,
    palette: FarmPalette,
    validationMessage: String
) {
    Text("Enter weight", color = palette.textPrimary, fontWeight = FontWeight.Black, fontSize = 26.sp)
    Text("Type the exact value for weight and age.", color = palette.textSecondary)
    OutlinedTextField(
        value = weightText,
        onValueChange = onWeightChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Weight (kg)") },
        placeholder = { Text("e.g. 450") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        shape = RoundedCornerShape(18.dp),
        colors = farmTextFieldColors(palette)
    )
    OutlinedTextField(
        value = ageText,
        onValueChange = onAgeChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Age (years)") },
        placeholder = { Text("e.g. 4") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        shape = RoundedCornerShape(18.dp),
        colors = farmTextFieldColors(palette)
    )
    if (validationMessage.isNotBlank()) {
        Text(validationMessage, color = Color(0xFFB3261E), fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
    }
}

@Composable
private fun MilkStepContent(
    milkYieldText: String,
    onMilkYieldChange: (String) -> Unit,
    palette: FarmPalette,
    validationMessage: String
) {
    Text("Milk yield", color = palette.textPrimary, fontWeight = FontWeight.Black, fontSize = 26.sp)
    Text("This input changes the feed recipe dynamically.", color = palette.textSecondary)
    Surface(shape = RoundedCornerShape(22.dp), color = palette.surfaceStrong) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Icon(Icons.Default.WaterDrop, contentDescription = null, tint = palette.accent)
                Text("${milkYieldText.toFloatOrNull() ?: 0f} L/day", color = palette.textPrimary, fontWeight = FontWeight.Black, fontSize = 28.sp)
            }
            Text("Higher milk yield increases concentrate and energy demand.", color = palette.textSecondary, lineHeight = 20.sp)
        }
    }
    OutlinedTextField(
        value = milkYieldText,
        onValueChange = onMilkYieldChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Milk yield (litres/day)") },
        placeholder = { Text("e.g. 10") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        shape = RoundedCornerShape(18.dp),
        colors = farmTextFieldColors(palette)
    )
    if (validationMessage.isNotBlank()) {
        Text(validationMessage, color = Color(0xFFB3261E), fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
    }
}

@Composable
private fun InputProgressSummary(
    breed: String,
    weightText: String,
    milkYieldText: String,
    palette: FarmPalette
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 420.dp),
        shape = RoundedCornerShape(20.dp),
        color = palette.surfaceStrong
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            InputSummaryChip("Breed", breed.ifBlank { "Not set" }, palette, Modifier.weight(1f))
            InputSummaryChip("Weight", if (weightText.isBlank()) "Not set" else "$weightText kg", palette, Modifier.weight(1f))
            InputSummaryChip("Milk", if (milkYieldText.isBlank()) "Not set" else "$milkYieldText L", palette, Modifier.weight(1f))
        }
    }
}

@Composable
private fun InputSummaryChip(title: String, value: String, palette: FarmPalette, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = palette.surface
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(title, color = palette.textSecondary, fontSize = 11.sp)
            Text(value, color = palette.textPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
    }
}

private fun Double.toInputText(defaultWhenZero: String = ""): String {
    if (this == 0.0) return defaultWhenZero
    return if (this % 1.0 == 0.0) {
        this.toInt().toString()
    } else {
        String.format(Locale.US, "%.1f", this)
    }
}
