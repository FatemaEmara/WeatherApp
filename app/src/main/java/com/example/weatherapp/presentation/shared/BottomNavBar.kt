package com.example.weatherapp.presentation.shared

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.weatherapp.navigation.NavItem

private val NavBg = Color(0xFF111B3A)
private val ActiveColor = Color(0xFF6C8EFF)
private val InactiveColor = Color(0xFF6B7BA4)

@Composable
fun BottomNavBar(navController: NavController) {

    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route

    NavigationBar(containerColor = NavBg) {
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
                    selectedIconColor = ActiveColor,
                    selectedTextColor = ActiveColor,
                    unselectedIconColor = InactiveColor,
                    unselectedTextColor = InactiveColor,
                    indicatorColor = ActiveColor.copy(alpha = 0.15f)
                )
            )
        }
    }
}
