package com.example.weatherapp.presentation.shared

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.weatherapp.navigation.NavItem
import com.example.weatherapp.ui.theme.AppTheme

@Composable
fun BottomNavBar(navController: NavController) {

    val colors = AppTheme.colors
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route

    NavigationBar(containerColor = colors.navBg) {
        NavItem.all.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navController.navigate(item.route) {
                            popUpTo(NavItem.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(text = item.label, fontSize = 11.sp)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colors.accent,
                    selectedTextColor = colors.accent,
                    unselectedIconColor = colors.navInactive,
                    unselectedTextColor = colors.navInactive,
                    indicatorColor = colors.accent.copy(alpha = 0.15f)
                )
            )
        }
    }
}