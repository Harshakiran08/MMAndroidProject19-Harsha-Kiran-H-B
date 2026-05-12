package com.pashuaahar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pashuaahar.ui.SharedViewModel

private data class BottomTab(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun MainScreen(rootNavController: NavController, sharedViewModel: SharedViewModel) {
    val shellNavController = rememberNavController()
    val palette = rememberFarmPalette(sharedViewModel.isDarkMode)
    val isK = sharedViewModel.languageCode == "kn"

    val bottomTabs = listOf(
        BottomTab("home_tab", if (isK) "ಮುಖಪುಟ" else "Home", Icons.Default.Home),
        BottomTab("comparison_tab", if (isK) "ಮೌಲ್ಯಗಳು" else "Values", Icons.Default.CurrencyRupee),
        BottomTab("history_tab", if (isK) "ಇತಿಹಾಸ" else "History", Icons.Default.Info),
        BottomTab("settings_tab", if (isK) "ಬೆಲೆಗಳು" else "Prices", Icons.Default.Science)
    )

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LogoBadge(
                            palette = palette,
                            containerSize = 64.dp,
                            logoSize = 50.dp
                        )
                        Column {
                            Text(
                                text = if (isK) "ಪಶು-ಆಹಾರ" else "Pashu-Aahar",
                                color = palette.accentStrong,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 23.sp
                            )
                            Text(
                                text = if (isK) "ಪಶು ಆಹಾರ ಕ್ಯಾಲ್ಕುಲೇಟರ್" else "Cattle Nutrition Calculator",
                                color = palette.textSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            shellNavController.navigate("profile_tab") {
                                popUpTo(shellNavController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        modifier = Modifier.background(Color.White.copy(alpha = 0.95f), RoundedCornerShape(18.dp))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = palette.accentStrong
                        )
                    }
                }
            }
        },
        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .navigationBarsPadding(),
                color = palette.accentStrong,
                shadowElevation = 16.dp,
                shape = RoundedCornerShape(26.dp)
            ) {
                val navBackStackEntry by shellNavController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    BottomBarTab(
                        modifier = Modifier.weight(1f),
                        item = bottomTabs[0],
                        selected = currentDestination?.hierarchy?.any { it.route == bottomTabs[0].route } == true,
                        onClick = {
                            shellNavController.navigate(bottomTabs[0].route) {
                                popUpTo(shellNavController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                    BottomBarTab(
                        modifier = Modifier.weight(1f),
                        item = bottomTabs[1],
                        selected = currentDestination?.hierarchy?.any { it.route == bottomTabs[1].route } == true,
                        onClick = {
                            shellNavController.navigate(bottomTabs[1].route) {
                                popUpTo(shellNavController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                    CenterCalculateTab(
                        modifier = Modifier.weight(1f),
                        onClick = { rootNavController.navigate("input") },
                        tint = palette.accentStrong
                    )
                    BottomBarTab(
                        modifier = Modifier.weight(1f),
                        item = bottomTabs[2],
                        selected = currentDestination?.hierarchy?.any { it.route == bottomTabs[2].route } == true,
                        onClick = {
                            shellNavController.navigate(bottomTabs[2].route) {
                                popUpTo(shellNavController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                    BottomBarTab(
                        modifier = Modifier.weight(1f),
                        item = bottomTabs[3],
                        selected = currentDestination?.hierarchy?.any { it.route == bottomTabs[3].route } == true,
                        onClick = {
                            shellNavController.navigate(bottomTabs[3].route) {
                                popUpTo(shellNavController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(palette.background)
                .padding(innerPadding)
                .padding(bottom = 12.dp)
        ) {
            NavHost(
                navController = shellNavController,
                startDestination = "home_tab"
            ) {
                composable("home_tab") { HomeScreen(shellNavController, rootNavController, sharedViewModel) }
                composable("comparison_tab") { ComparisonScreen(rootNavController, sharedViewModel, showBackButton = false) }
                composable("history_tab") { HistoryScreen(rootNavController, sharedViewModel) }
                composable("settings_tab") { SettingsScreen(sharedViewModel) }
                composable("profile_tab") { ProfileScreen(sharedViewModel, rootNavController) }
            }
        }
    }
}

@Composable
private fun BottomBarTab(
    modifier: Modifier,
    item: BottomTab,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Surface(
            onClick = onClick,
            shape = CircleShape,
            color = if (selected) Color.White.copy(alpha = 0.22f) else Color.Transparent
        ) {
            Box(
                modifier = Modifier.size(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        Text(
            item.label,
            color = Color.White,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 10.sp
        )
    }
}

@Composable
private fun CenterCalculateTab(
    modifier: Modifier,
    onClick: () -> Unit,
    tint: Color
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Surface(
            onClick = onClick,
            shape = CircleShape,
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Box(
                modifier = Modifier.size(42.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Calculate,
                    contentDescription = "Calculate",
                    tint = tint,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
        Text(
            text = "Calculate",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp
        )
    }
}
