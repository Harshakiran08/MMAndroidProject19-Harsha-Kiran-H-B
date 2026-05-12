package com.pashuaahar.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pashuaahar.ui.SharedViewModel
import com.pashuaahar.ui.theme.Strings

@Composable
fun ProfileScreen(sharedViewModel: SharedViewModel, rootNavController: NavController) {
    val palette = rememberFarmPalette(sharedViewModel.isDarkMode)
    val context = LocalContext.current
    val historyList by sharedViewModel.allHistory.collectAsState(initial = emptyList())
    val isLoggedIn = sharedViewModel.currentUsername.isNotBlank()
    val isK = sharedViewModel.languageCode == "kn"

    FarmScrollPage(palette = palette) {
        ModuleHeroCard(
            palette = palette,
            title = if (isLoggedIn) Strings.get("farmer_profile", isK) else Strings.get("welcome_to_profile", isK),
            subtitle = if (isLoggedIn) {
                "${Strings.get("profile_signed_in", isK)} ${sharedViewModel.currentUsername}${Strings.get("profile_manage", isK)}"
            } else {
                Strings.get("profile_guest_desc", isK)
            }
        )

        // Profile Card
        Card(
            shape = farmCardShape(),
            colors = CardDefaults.cardColors(containerColor = palette.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .background(palette.accentSoft, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = palette.accent, modifier = Modifier.size(34.dp))
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = if (isLoggedIn) sharedViewModel.currentUsername else Strings.get("guest_farmer", isK),
                            color = palette.textPrimary,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = if (isLoggedIn) "${historyList.size} ${Strings.get("saved_history_entries", isK)}" else Strings.get("no_signin_session", isK),
                            color = palette.textSecondary
                        )
                    }
                }

                if (isLoggedIn) {
                    Button(
                        onClick = {
                            sharedViewModel.logout()
                            showFarmToast(context, Strings.get("signed_out", isK), palette)
                            rootNavController.navigate("login") {
                                popUpTo("main") { inclusive = false }
                            }
                        },
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = palette.accentStrong, contentColor = Color.White)
                    ) {
                        Icon(Icons.Default.ExitToApp, contentDescription = null)
                        Text(Strings.get("sign_out", isK), modifier = Modifier.padding(start = 8.dp), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                } else {
                    Button(
                        onClick = { rootNavController.navigate("login") },
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = palette.accent, contentColor = Color.White)
                    ) {
                        Icon(Icons.Default.Login, contentDescription = null)
                        Text(Strings.get("sign_in", isK), modifier = Modifier.padding(start = 8.dp), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }

                    Button(
                        onClick = { rootNavController.navigate("register") },
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = palette.highlight, contentColor = palette.textPrimary)
                    ) {
                        Icon(Icons.Default.PersonAdd, contentDescription = null)
                        Text(Strings.get("create_account", isK), modifier = Modifier.padding(start = 8.dp), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }

        // Language & App Settings
        ProfileSettingsBlock(
            title = Strings.get("app_preferences", isK),
            subtitle = Strings.get("customize_experience", isK),
            icon = { Icon(Icons.Default.Language, contentDescription = null, tint = palette.accent) },
            palette = palette
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(Strings.get("kannada_language", isK), color = palette.textPrimary, fontWeight = FontWeight.SemiBold)
                Switch(
                    checked = isK,
                    onCheckedChange = { 
                        sharedViewModel.setLanguage(if (it) "kn" else "en") 
                        showFarmToast(context, if (it) "Kannada enabled" else "English enabled", palette)
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = palette.accentStrong,
                        checkedBorderColor = palette.accentStrong,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = palette.surfaceMuted,
                        uncheckedBorderColor = palette.border
                    )
                )
            }
            

        }

        // App Version
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = palette.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 20.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = palette.textSecondary)
                    Text(Strings.get("app_version", isK), color = palette.textPrimary, fontWeight = FontWeight.SemiBold)
                }
                Text("1.0.0", color = palette.textSecondary)
            }
        }
    }
}

@Composable
private fun ProfileSettingsBlock(
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
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = RoundedCornerShape(16.dp), color = palette.surfaceStrong) {
                    Row(
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
