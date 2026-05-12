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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

private const val LOGIN_MIN_PASSWORD_LENGTH = 8
private const val PASSWORD_RULE_MESSAGE = "Use at least 8 characters with 1 letter and 1 number."

@Composable
fun LoginScreen(navController: NavController, sharedViewModel: SharedViewModel) {
    val palette = rememberFarmPalette(sharedViewModel.isDarkMode)
    val isK = sharedViewModel.languageCode == "kn"
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showResetDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(sharedViewModel.pendingLoginUsername, sharedViewModel.pendingLoginPassword) {
        if (sharedViewModel.pendingLoginUsername.isNotBlank()) {
            username = sharedViewModel.pendingLoginUsername
            password = sharedViewModel.pendingLoginPassword
        }
    }

    FarmBackground(palette = palette) {
        if (showResetDialog) {
            ResetPasswordDialog(
                isKannada = isK,
                palette = palette,
                onDismiss = { showResetDialog = false },
                onPasswordReset = { resetUsername, newPassword, onResult ->
                    coroutineScope.launch {
                        val success = sharedViewModel.resetPassword(resetUsername, newPassword)
                        onResult(success)
                        if (success) {
                            username = resetUsername.trim()
                            password = newPassword
                            errorMessage = Strings.get("reset_password_success", isK)
                            showResetDialog = false
                        }
                    }
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
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
                text = Strings.get("welcome_back", isK),
                color = palette.textSecondary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )

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
                        text = Strings.get("sign_in", isK),
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

                    if (errorMessage.isNotBlank()) {
                        Text(
                            text = errorMessage,
                            color = Color(0xFFB3261E),
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Button(
                        onClick = {
                            if (username.isBlank() || password.isBlank()) {
                                errorMessage = Strings.get("fields_empty", isK)
                                return@Button
                            }
                            if (!isPasswordValidForLogin(password)) {
                                errorMessage = "Enter a valid password format to continue."
                                return@Button
                            }
                            coroutineScope.launch {
                                val success = sharedViewModel.login(username.trim(), password)
                                if (success) {
                                    navController.navigate("main") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                } else {
                                    errorMessage = Strings.get("invalid_login", isK)
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
                        Text(text = Strings.get("sign_in", isK), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }

                    TextButton(
                        onClick = { showResetDialog = true },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = Strings.get("reset_password", isK),
                            color = palette.accentStrong,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    TextButton(
                        onClick = { navController.navigate("register") },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = Strings.get("need_account", isK),
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

private fun isPasswordValidForLogin(password: String): Boolean {
    return password.length >= LOGIN_MIN_PASSWORD_LENGTH &&
        password.any { it.isLetter() } &&
        password.any { it.isDigit() }
}

@Composable
private fun ResetPasswordDialog(
    isKannada: Boolean,
    palette: FarmPalette,
    onDismiss: () -> Unit,
    onPasswordReset: (username: String, newPassword: String, onResult: (Boolean) -> Unit) -> Unit
) {
    var username by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = Strings.get("reset_password", isKannada),
                fontWeight = FontWeight.Bold,
                color = palette.textPrimary
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = Strings.get("reset_password_desc", isKannada),
                    color = palette.textSecondary,
                    lineHeight = 18.sp
                )
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it; errorMessage = "" },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(Strings.get("username", isKannada)) },
                    singleLine = true,
                    shape = RoundedCornerShape(18.dp),
                    colors = farmTextFieldColors(palette)
                )
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it; errorMessage = "" },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(Strings.get("new_password", isKannada)) },
                    visualTransformation = if (newPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { newPasswordVisible = !newPasswordVisible }) {
                            Icon(
                                imageVector = if (newPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = Strings.get(if (newPasswordVisible) "hide_password" else "show_password", isKannada),
                                tint = palette.accentStrong
                            )
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(18.dp),
                    colors = farmTextFieldColors(palette)
                )
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it; errorMessage = "" },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(Strings.get("confirm_new_password", isKannada)) },
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                imageVector = if (confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = Strings.get(if (confirmPasswordVisible) "hide_password" else "show_password", isKannada),
                                tint = palette.accentStrong
                            )
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(18.dp),
                    colors = farmTextFieldColors(palette)
                )
                if (errorMessage.isNotBlank()) {
                    Text(
                        text = errorMessage,
                        color = Color(0xFFB3261E),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (username.isBlank() || newPassword.isBlank() || confirmPassword.isBlank()) {
                        errorMessage = Strings.get("fields_empty", isKannada)
                        return@TextButton
                    }
                    if (!isPasswordValidForLogin(newPassword)) {
                        errorMessage = PASSWORD_RULE_MESSAGE
                        return@TextButton
                    }
                    if (newPassword != confirmPassword) {
                        errorMessage = Strings.get("password_mismatch", isKannada)
                        return@TextButton
                    }
                    onPasswordReset(username, newPassword) { success ->
                        if (!success) {
                            errorMessage = Strings.get("user_not_found", isKannada)
                        }
                    }
                },
                colors = ButtonDefaults.textButtonColors(contentColor = palette.accentStrong)
            ) {
                Text(Strings.get("reset_password", isKannada), fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = palette.textSecondary)
            ) {
                Text(Strings.get("cancel", isKannada))
            }
        },
        containerColor = palette.surface,
        tonalElevation = 0.dp,
        iconContentColor = palette.accentStrong,
        titleContentColor = palette.textPrimary,
        textContentColor = palette.textPrimary
    )
}
