package com.example.weatherapp.navigation


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.weatherapp.data.network.WeatherNetwork
import com.example.weatherapp.data.weather.WeatherRepository
import com.example.weatherapp.data.weather.datasource.local.AppDatabase
import com.example.weatherapp.data.weather.datasource.remote.WeatherRemoteDataSourceImpl
import com.example.weatherapp.data.weather.model.FavoriteLocation
import com.example.weatherapp.presentation.fav.view.FavoriteScreen
import com.example.weatherapp.presentation.fav.viewmodel.FavoriteFactory
import com.example.weatherapp.presentation.fav.viewmodel.FavoriteViewModel
import com.example.weatherapp.presentation.forcastdetail.view.ForecastDetailScreen
import com.example.weatherapp.presentation.home.view.HomeScreen
import com.example.weatherapp.presentation.home.viewmodel.HomeFactory
import com.example.weatherapp.presentation.home.viewmodel.HomeViewModel
import com.example.weatherapp.presentation.shared.map.MapPickerScreen
import com.example.weatherapp.presentation.shared.map.MapPickerSource

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val repository = remember {
        WeatherRepository(
            remoteDataSource = WeatherRemoteDataSourceImpl(WeatherNetwork.weatherService),
            favoriteDao = AppDatabase.getInstance(context).favoriteDao()
        )
    }

    val favoriteViewModel: FavoriteViewModel = viewModel(factory = FavoriteFactory(repository))

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

        composable("map_picker/favorite") {
            MapPickerScreen(
                source           = MapPickerSource.FAVORITE,
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
                navController    = navController
            )
        }

        composable(
            route = "forecast_detail/{locationId}",
            arguments = listOf(
                navArgument("locationId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val locationId = backStackEntry.arguments?.getInt("locationId") ?: return@composable
            ForecastDetailScreen(
                locationId = locationId,
                navController = navController,
                repository = repository
            )
        }

        composable(NavItem.Alerts.route) { PlaceholderScreen("Alerts") }
        composable(NavItem.Settings.route) { PlaceholderScreen("Settings") }
    }
}

@Composable
private fun PlaceholderScreen(title: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = title, color = Color.White, fontSize = 24.sp)
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
