package com.example.weatherapp.presentation.home.view

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.data.weather.model.CurrentWeatherResponse
import com.example.weatherapp.data.weather.model.ForecastItem
import com.example.weatherapp.data.weather.model.ForecastResponse
import com.example.weatherapp.presentation.home.components.DailyDetailsCard
import com.example.weatherapp.presentation.home.components.DailyRow
import com.example.weatherapp.presentation.home.components.HourlyCard
import com.example.weatherapp.presentation.home.components.SectionTitle
import com.example.weatherapp.presentation.home.components.SunriseSunsetRow
import com.example.weatherapp.presentation.home.components.TemperatureDisplay
import com.example.weatherapp.presentation.home.components.TopWeatherBar
import com.example.weatherapp.presentation.home.viewmodel.HomeViewModel
import com.example.weatherapp.presentation.shared.iconRes
import com.example.weatherapp.ui.theme.AppTheme
import com.example.weatherapp.utils.LocationHelper
import com.example.weatherapp.utils.ResponseState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(viewModel: HomeViewModel) {

    val colors = AppTheme.colors
    val context = LocalContext.current
    val locationHelper = remember { LocationHelper(context) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        if (granted && locationHelper.isLocationEnabled()) {
            locationHelper.getFreshLocation { location ->
                location?.let {
                    viewModel.loadCurrentWeather(it.latitude, it.longitude)
                    viewModel.loadForecast(it.latitude, it.longitude)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    val weatherState by viewModel.weatherState.collectAsState()
    val forecastState by viewModel.forecastState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(colors.bgTop, colors.bgBottom)))
    ) {
        when (weatherState) {
            is ResponseState.Loading -> FullScreenMessage("Loading weather…")
            is ResponseState.Failure -> FullScreenMessage(
                (weatherState as ResponseState.Failure).error
            )

            is ResponseState.Success -> {
                val current = (weatherState as ResponseState.Success).data
                val forecast = (forecastState as? ResponseState.Success)?.data
                WeatherContent(current = current, forecast = forecast)
            }
        }
    }
}

@Composable
private fun WeatherContent(
    current: CurrentWeatherResponse,
    forecast: ForecastResponse?
) {
    val hourlyItems = forecast?.todayHourlyItems() ?: emptyList()
    val dailyItems = forecast?.dailyItems() ?: emptyList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopWeatherBar(
            description = current.weather.firstOrNull()?.description
                ?.replaceFirstChar { it.uppercase() } ?: "—",
            feelsLike = current.main.feelsLike,
            dateString = current.dt.toDateString()
        )

        Spacer(Modifier.height(16.dp))

        TemperatureDisplay(
            iconRes = iconRes(current.weather.firstOrNull()?.icon ?: "01d"),
            tempCelsius = current.main.temp,
            cityName = current.name,
            countryCode = current.sys.country
        )

        Spacer(Modifier.height(12.dp))

        SunriseSunsetRow(
            sunriseTime = current.sys.sunrise.toTimeString(),
            sunsetTime = current.sys.sunset.toTimeString()
        )

        Spacer(Modifier.height(28.dp))

        SectionTitle(
            normal = "Hourly",
            bold = "Details"
        )
        Spacer(Modifier.height(12.dp))

        if (hourlyItems.isEmpty()) {
            NoDataText("No hourly data available")
        } else {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(hourlyItems) { item ->
                    HourlyCard(
                        item = item,
                        hourLabel = item.dt.toHourLabel(),
                        iconRes = iconRes(item.weather.firstOrNull()?.icon ?: "01d"),

                        )
                }
            }
        }

        Spacer(Modifier.height(28.dp))

        SectionTitle(
            normal = "Daily",
            bold = "Details"
        )
        Spacer(Modifier.height(12.dp))

        DailyDetailsCard(
            pressure = current.main.pressure,
            windSpeed = current.wind.speed,
            humidity = current.main.humidity,
            clouds = current.clouds.all
        )

        Spacer(Modifier.height(28.dp))

        SectionTitle(
            normal = "Next 7",
            bold = "Days"
        )
        Spacer(Modifier.height(12.dp))

        if (dailyItems.isEmpty()) {
            NoDataText("No forecast data available")
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                dailyItems.forEachIndexed { index, item ->
                    DailyRow(
                        item = item,
                        isToday = index == 0,
                        weekday = item.dt.toWeekday(),
                        shortDate = item.dt.toShortDate(),
                        iconRes = iconRes(item.weather.firstOrNull()?.icon ?: "01d")
                    )
                }
            }
        }

        Spacer(Modifier.height(80.dp))
    }
}

@Composable
private fun FullScreenMessage(message: String) {
    val colors = AppTheme.colors
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = message,
            color = colors.textPrimary,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun NoDataText(message: String) {
    Text(
        text = message,
        color = AppTheme.colors.textMuted,
        fontSize = 13.sp
    )
}

private fun Long.toTimeString(): String =
    SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(this * 1000L))

private fun Long.toDateString(): String =
    SimpleDateFormat("EEE, dd MMM", Locale.getDefault()).format(Date(this * 1000L))

private fun Long.toHourLabel(): String =
    SimpleDateFormat("hha", Locale.getDefault()).format(Date(this * 1000L)).uppercase()

private fun Long.toWeekday(): String =
    SimpleDateFormat("EEEE", Locale.getDefault()).format(Date(this * 1000L))

private fun Long.toShortDate(): String =
    SimpleDateFormat("EEE, dd MMM", Locale.getDefault()).format(Date(this * 1000L))


private fun ForecastResponse.todayHourlyItems(): List<ForecastItem> {
    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    return list.filter { it.dtTxt.startsWith(today) }
}

private fun ForecastResponse.dailyItems(): List<ForecastItem> {
    val byDay = list.groupBy { item ->
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(item.dt * 1000L))
    }
    return byDay.values.map { slots ->
        slots.firstOrNull { it.dtTxt.contains("12:00:00") } ?: slots.first()
    }.sortedBy { it.dt }
}