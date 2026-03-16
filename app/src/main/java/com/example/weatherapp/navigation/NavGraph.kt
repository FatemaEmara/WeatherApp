package com.example.weatherapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.weatherapp.data.weather.AppRepository
import com.example.weatherapp.data.weather.model.FavoriteLocation
import com.example.weatherapp.presentation.alert.view.AlertsScreen
import com.example.weatherapp.presentation.alert.viewmodel.AlertsFactory
import com.example.weatherapp.presentation.alert.viewmodel.AlertsViewModel
import com.example.weatherapp.presentation.fav.view.FavoriteScreen
import com.example.weatherapp.presentation.fav.viewmodel.FavoriteFactory
import com.example.weatherapp.presentation.fav.viewmodel.FavoriteViewModel
import com.example.weatherapp.presentation.forcastdetail.view.ForecastDetailScreen
import com.example.weatherapp.presentation.forcastdetail.viewmodel.ForecastDetailFactory
import com.example.weatherapp.presentation.forcastdetail.viewmodel.ForecastDetailViewModel
import com.example.weatherapp.presentation.home.view.HomeScreen
import com.example.weatherapp.presentation.home.viewmodel.HomeFactory
import com.example.weatherapp.presentation.home.viewmodel.HomeViewModel
import com.example.weatherapp.presentation.settings.view.SettingsScreen
import com.example.weatherapp.presentation.settings.viewmodel.SettingsViewModel
import com.example.weatherapp.presentation.shared.map.MapPickerScreen
import com.example.weatherapp.presentation.shared.map.MapPickerSource

@Composable
fun AppNavHost(
    navController: NavHostController,
    repository: AppRepository,
    settingsViewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current


    val favoriteViewModel: FavoriteViewModel = viewModel(factory = FavoriteFactory(repository))
    val alertsViewModel: AlertsViewModel = viewModel(factory = AlertsFactory(repository, context))
    NavHost(
        navController = navController,
        startDestination = NavItem.Home.route,
        modifier = modifier
    ) {

        composable(NavItem.Home.route) {
            val viewModel: HomeViewModel = viewModel(factory = HomeFactory(repository))
            HomeScreen(viewModel = viewModel)
        }

        composable(NavItem.Favorite.route) {
            FavoriteScreen(viewModel = favoriteViewModel, navController = navController)
        }
        composable(NavItem.Settings.route) {
            SettingsScreen(viewModel = settingsViewModel, navController = navController)
        }

        composable("map_picker/favorite") {
            MapPickerScreen(
                source = MapPickerSource.FAVORITE,
                onLocationPicked = { lat, lon, city, country ->
                    favoriteViewModel.addFavorite(
                        FavoriteLocation(
                            cityName = city,
                            countryCode = country,
                            lat = lat,
                            lon = lon
                        )
                    )
                },
                navController = navController
            )
        }
        composable("map_picker/settings") {
            MapPickerScreen(
                source = MapPickerSource.SETTINGS,
                onLocationPicked = { lat, lon, _, _ ->
                    settingsViewModel.saveManualLocation(lat, lon)
                },
                navController = navController
            )
        }

        composable(
            route = "forecast_detail/{locationId}",
            arguments = listOf(
                navArgument("locationId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val locationId = backStackEntry.arguments?.getInt("locationId") ?: return@composable
            val viewModel: ForecastDetailViewModel = viewModel(
                factory = ForecastDetailFactory(
                    repository,
                    locationId = locationId
                )
            )
            ForecastDetailScreen(
                locationId = locationId,
                navController = navController,
                repository = repository,
                viewModel = viewModel,
            )
        }

        composable(NavItem.Alerts.route) {
            AlertsScreen(viewModel = alertsViewModel)
        }


    }
}


sealed class NavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Home : NavItem(
        route = "home",
        label = "Home",
        icon = Icons.Filled.Home
    )

    object Favorite : NavItem(
        route = "favorite",
        label = "Favorite",
        icon = Icons.Filled.FavoriteBorder
    )

    object Alerts : NavItem(
        route = "alerts",
        label = "Alerts",
        icon = Icons.Filled.Notifications
    )

    object Settings : NavItem(
        route = "settings",
        label = "Settings",
        icon = Icons.Filled.Settings
    )

    companion object {
        val all = listOf(Home, Favorite, Alerts, Settings)
    }
}
