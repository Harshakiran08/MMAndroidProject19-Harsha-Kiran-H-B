package com.pashuaahar.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pashuaahar.ui.SharedViewModel
import com.pashuaahar.ui.theme.Strings
import kotlinx.coroutines.launch

private const val MIN_PASSWORD_LENGTH = 8

@Composable
fun RegisterScreen(navController: NavController, sharedViewModel: SharedViewModel) {
    val palette = rememberFarmPalette(sharedViewModel.isDarkMode)
    val isK = sharedViewModel.languageCode == "kn"
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    FarmBackground(palette = palette) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.Start)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = palette.accentStrong
                )
            }

            LogoBadge(
                palette = palette,
                containerSize = 92.dp,
                logoSize = 70.dp
            )
            Text(
                text = if (isK) "à²ªà²¶à³-à²†à²¹à²¾à²°" else "Pashu-Aahar",
                color = palette.accentStrong,
                fontWeight = FontWeight.Black,
                fontSize = 42.sp
            )
            Text(
                text = Strings.get("create_account_title", isK),
                color = palette.textSecondary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = palette.highlight
            ) {
                Text(
                    text = Strings.get("create_account_desc", isK),
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp),
                    color = palette.textPrimary,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 19.sp
                )
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = Strings.get("create_account", isK),
                        color = palette.textPrimary,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp
                    )

                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it; errorMessage = "" },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(Strings.get("username", isK)) },
                        singleLine = true,
                        shape = RoundedCornerShape(18.dp),
                        colors = farmTextFieldColors(palette)
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it; errorMessage = "" },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(Strings.get("password", isK)) },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = Strings.get(if (passwordVisible) "hide_password" else "show_password", isK)
                                )
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(18.dp),
                        colors = farmTextFieldColors(palette)
                    )
                    Text(
                        text = "Password must be at least 8 characters and include 1 letter and 1 number.",
                        color = palette.textSecondary,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it; errorMessage = "" },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(Strings.get("confirm_password", isK)) },
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(
                                    imageVector = if (confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = Strings.get(if (confirmPasswordVisible) "hide_password" else "show_password", isK)
                                )
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(18.dp),
                        colors = farmTextFieldColors(palette)
                    )

                    if (errorMessage.isNotBlank()) {
                        Text(text = errorMessage, color = Color(0xFFB3261E), fontWeight = FontWeight.SemiBold)
                    }

                    Button(
                        onClick = {
                            if (username.isBlank() || password.isBlank()) {
                                errorMessage = Strings.get("fields_empty", isK)
                                return@Button
                            }
                            if (!isPasswordValid(password)) {
                                errorMessage = "Use at least 8 characters with 1 letter and 1 number."
                                return@Button
                            }
                            if (password != confirmPassword) {
                                errorMessage = Strings.get("password_mismatch", isK)
                                return@Button
                            }
                            coroutineScope.launch {
                                val success = sharedViewModel.register(username.trim(), password)
                                if (success) {
                                    navController.navigate("login") {
                                        popUpTo("register") { inclusive = true }
                                    }
                                } else {
                                    errorMessage = Strings.get("user_exists", isK)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = palette.accent,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = Strings.get("create_account", isK), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }

                    TextButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = Strings.get("already_registered", isK),
                            color = palette.accentStrong,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Text(
                text = "App version 1.0.0",
                color = palette.textSecondary,
                fontSize = 13.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

private fun isPasswordValid(password: String): Boolean {
    return password.length >= MIN_PASSWORD_LENGTH &&
        password.any { it.isLetter() } &&
        password.any { it.isDigit() }
}
