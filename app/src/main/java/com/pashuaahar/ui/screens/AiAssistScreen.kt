package com.pashuaahar.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pashuaahar.ui.SharedViewModel
import com.pashuaahar.ui.theme.Strings

@Composable
fun AiAssistScreen(navController: NavController, sharedViewModel: SharedViewModel) {
    val palette = rememberFarmPalette(sharedViewModel.isDarkMode)
    val isK = sharedViewModel.languageCode == "kn"
    var message by remember { mutableStateOf("") }
    val chatItems = remember {
        mutableStateListOf(
            Strings.get("ai_greeting", isK)
        )
    }

    FarmScrollPage(
        palette = palette,
        showTopBar = true,
        title = "AI Assistant",
        onBackClick = { navController.popBackStack() }
    ) {
        Card(
            shape = farmCardShape(),
            colors = CardDefaults.cardColors(containerColor = palette.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            androidx.compose.foundation.layout.Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(Strings.get("ai_assist_title", isK), color = palette.textPrimary, fontWeight = FontWeight.Black, fontSize = 26.sp)
                Text(Strings.get("ai_assist_desc", isK), color = palette.textSecondary, lineHeight = 20.sp)
            }
        }

        chatItems.forEachIndexed { index, item ->
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (index % 2 == 0) palette.surface else palette.accentSoft
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = item,
                    modifier = Modifier.padding(16.dp),
                    color = palette.textPrimary,
                    lineHeight = 20.sp
                )
            }
        }

        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(Strings.get("ask_anything", isK)) },
            shape = RoundedCornerShape(18.dp),
            colors = farmTextFieldColors(palette)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    if (message.isBlank()) return@Button
                    chatItems += message
                    chatItems += generateAiReply(message, sharedViewModel, isK)
                    message = ""
                },
                modifier = Modifier.weight(1f).height(54.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = palette.accent)
            ) {
                Text(Strings.get("send", isK), fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.weight(1f).height(54.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = palette.accentStrong)
            ) {
                Text(Strings.get("back_to_home_screen", isK), fontWeight = FontWeight.Bold)
            }
        }
    }
}

private fun generateAiReply(question: String, sharedViewModel: SharedViewModel, isKannada: Boolean): String {
    val q = question.lowercase()
    
    // Greeting
    if (q in listOf("hi", "hello", "hey", "namaskara", "ನಮಸ್ಕಾರ")) {
        return if (isKannada) "ನಮಸ್ಕಾರ! ನಾನು ಪಶು-ಆಹಾರ ಆಪ್ ಸಹಾಯಕಿ. ಆಪ್ ಬಗ್ಗೆ ಅಥವಾ ಡೇಟಾ ಬಗ್ಗೆ ಕೇಳಿ." 
        else "Hello! I am your Pashu-Aahar assistant. Ask me anything about the app or your data."
    }

    // Dynamic Context Building
    if ("market" in q || "price" in q || "ಬೆಲೆ" in q || "ಮಾರುಕಟ್ಟೆ" in q) {
        return if (isKannada) {
            "ಪ್ರಸ್ತುತ ${sharedViewModel.marketLocation} ನಲ್ಲಿ: ಮಕ್ಕೆಜೋಳ ₹${sharedViewModel.priceMaize}, ಹತ್ತಿಬೀಜ ₹${sharedViewModel.priceCottonseed}, ಬ್ರಾನ್ ₹${sharedViewModel.priceBran}. ನೀವು ಇವುಗಳನ್ನು ಪ್ರೊಫೈಲ್ ಟ್ಯಾಬ್ ನಲ್ಲಿ ಬದಲಾಯಿಸಬಹುದು."
        } else {
            "Currently in ${sharedViewModel.marketLocation}: Maize is ₹${sharedViewModel.priceMaize}, Cottonseed ₹${sharedViewModel.priceCottonseed}, Bran ₹${sharedViewModel.priceBran}. You can update these anytime in the Profile tab."
        }
    }

    if ("profile" in q || "setting" in q || "dark mode" in q || "language" in q || "ಭಾಷೆ" in q || "ಪ್ರೊಫೈಲ್" in q) {
        return if (isKannada) {
            "ಪ್ರೊಫೈಲ್ ಟ್ಯಾಬ್‌ನಲ್ಲಿ ನೀವು ಡಾರ್ಕ್ ಮೋಡ್ ಆನ್/ಆಫ್ ಮಾಡಬಹುದು, ಭಾಷೆ ಬದಲಾಯಿಸಬಹುದು, ಹಾಗೂ ಮಾರುಕಟ್ಟೆ ಬೆಲೆಗಳನ್ನು ನವೀಕರಿಸಬಹುದು. ಸದ್ಯ ನೀವು ${if (sharedViewModel.currentUsername.isBlank()) "ಗೆಸ್ಟ್ ಆಗಿ" else sharedViewModel.currentUsername + " ಆಗಿ"} ಬಳಸುತ್ತಿದ್ದೀರಿ."
        } else {
            "In the Profile tab, you can toggle Dark Mode, switch between English/Kannada, and update your local market prices. Currently logged in as: ${sharedViewModel.currentUsername.ifBlank { "Guest" }}."
        }
    }

    if ("history" in q || "past" in q || "saved" in q || "ಇತಿಹಾಸ" in q || "ಹಿಂದಿನ" in q) {
        return if (isKannada) {
            "ಹಿಸ್ಟರಿ ಟ್ಯಾಬ್ (ಕೆಳಗಿನ ನ್ಯಾವಿಗೇಶನ್) ನಿಮ್ಮ ಹಿಂದಿನ ಎಲ್ಲಾ ಲೆಕ್ಕಾಚಾರಗಳನ್ನು ಉಳಿಸುತ್ತದೆ."
        } else {
            "The History tab in the bottom bar keeps all your past calculations."
        }
    }

    if ("calculate" in q || "recipe" in q || "breed" in q || "milk" in q || "ಕ್ಯಾಲ್ಕುಲೇಟರ್" in q || "ರೆಸಿಪಿ" in q || "ಹಾಲು" in q) {
        return if (isKannada) {
            "ಹೋಮ್ ಸ್ಕ್ರೀನ್ ನಲ್ಲಿ 'ಕ್ಯಾಲ್ಕುಲೇಟರ್' ತೆರೆಯಿರಿ. ಅಲ್ಲಿ ತಳಿ, ತೂಕ ಮತ್ತು ಹಾಲಿನ ವಿವರ ನೀಡಿ ಫೀಡ್ ರೆಸಿಪಿ ಪಡೆಯಿರಿ. ಕೊನೆಯದಾಗಿ ನೀವು ${sharedViewModel.selectedBreed.ifBlank { "ಜರ್ಸಿ" }} ತಳಿಯನ್ನು ಆಯ್ಕೆ ಮಾಡಿದ್ದೀರಿ."
        } else {
            "Start the Calculator from the Home tab. Enter breed, weight, and milk yield to generate a recipe. Your last selected breed was ${sharedViewModel.selectedBreed.ifBlank { "Jersey" }}."
        }
    }
    
    if ("values" in q || "cost" in q || "savings" in q || "ವೆಚ್ಚ" in q || "ಉಳಿತಾಯ" in q) {
        return if (isKannada) {
            "ಮೌಲ್ಯಗಳು (Values) ಟ್ಯಾಬ್ ನಲ್ಲಿ ನಿಮ್ಮ ಕೊನೆಯ ರೆಸಿಪಿಯ ವೆಚ್ಚ, ಉಳಿತಾಯ ಮತ್ತು ಮಾರುಕಟ್ಟೆ ಚಾರ್ಟ್ ಅನ್ನು ನೋಡಬಹುದು."
        } else {
            "The Values tab breaks down your recipe costs, savings compared to commercial feed, and visualizes it in a chart."
        }
    }

    if ("tips" in q || "video" in q || "care" in q || "ಟಿಪ್ಸ್" in q || "ವಿಡಿಯೋ" in q) {
        return if (isKannada) {
            "ಕ್ಯಾಲ್ಕುಲೇಟರ್ ಕೊನೆಯಲ್ಲಿ, ದನದ ಆರೈಕೆಗಾಗಿ ನೀರು, ಫೈಬರ್ ಇತ್ಯಾದಿಗಳ ಬಗ್ಗೆ ಕೆಲವು ವಿಡಿಯೋ ಟಿಪ್ಸ್ ನೋಡಬಹುದು."
        } else {
            "At the end of the calculator flow, we provide actionable tips on roughage, clean water, and observing your cattle's health."
        }
    }

    // Catch-all Generative Mock
    return if (isKannada) {
        "ನಾನು ಆಫ್‌ಲೈನ್ ಸಹಾಯಕ. ನಿಮ್ಮ ಪ್ರಶ್ನೆ: '${question}'. ದಯವಿಟ್ಟು ಮಾರುಕಟ್ಟೆ, ಕ್ಯಾಲ್ಕುಲೇಟರ್, ಪ್ರೊಫೈಲ್, ಸೆಟ್ಟಿಂಗ್ಸ್ ಅಥವಾ ಇತಿಹಾಸದ ಬಗ್ಗೆ ಕೇಳಿ."
    } else {
        "As an offline assistant, I analyze your keywords. You asked about: '${question}'. Try asking me specifically about the Calculator, History, Market Prices, Profile, or Settings!"
    }
}
