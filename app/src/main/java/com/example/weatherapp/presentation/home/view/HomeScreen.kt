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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
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

    val units by viewModel.units.collectAsState()
    val windUnit by viewModel.windUnit.collectAsState()
    val lang by viewModel.lang.collectAsState()
    val locationMode by viewModel.locationMode.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        if (granted && locationHelper.isLocationEnabled()) {
            locationHelper.getFreshLocation { location ->
                location?.let { viewModel.loadWeather(it.latitude, it.longitude) }
            }
        }
    }

    LaunchedEffect(locationMode) {
        if (locationMode == "gps") {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    val weatherState by viewModel.weatherState.collectAsState()
    val forecastState by viewModel.forecastState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(colors.bgTop, colors.bgBottom)))
    ) {
        when (weatherState) {
            is ResponseState.Loading -> LoadingScreen()
            is ResponseState.Failure -> FullScreenMessage(
                (weatherState as ResponseState.Failure).error
            )

            is ResponseState.Success -> {
                val current = (weatherState as ResponseState.Success).data
                val forecast = (forecastState as? ResponseState.Success)?.data
                WeatherContent(
                    current = current,
                    forecast = forecast,
                    units = units,
                    windUnit = windUnit,
                    lang = lang
                )
            }
        }
    }
}

@Composable
private fun WeatherContent(
    current: CurrentWeatherResponse,
    forecast: ForecastResponse?,
    units: String,
    windUnit: String,
    lang: String
) {
    val locale = if (lang == "ar") Locale("ar") else Locale.ENGLISH
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
                ?.let { if (lang == "ar") it else it.replaceFirstChar { c -> c.uppercase() } }
                ?: "—",
            feelsLike = current.main.feelsLike,
            dateString = current.dt.toDateString(locale),
            units = units,
            lang = lang
        )

        Spacer(Modifier.height(16.dp))

        TemperatureDisplay(
            iconRes = iconRes(current.weather.firstOrNull()?.icon ?: "01d"),
            tempCelsius = current.main.temp,
            cityName = current.name,
            countryCode = current.sys.country,
            units = units
        )

        Spacer(Modifier.height(12.dp))

        SunriseSunsetRow(
            sunriseTime = current.sys.sunrise.toTimeString(locale),
            sunsetTime = current.sys.sunset.toTimeString(locale)
        )

        Spacer(Modifier.height(28.dp))

        SectionTitle(normal = stringResource(R.string.hourly), bold = stringResource(R.string.details))
        Spacer(Modifier.height(12.dp))

        if (hourlyItems.isEmpty()) {
            NoDataText(stringResource(R.string.no_hourly_data))
        } else {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(hourlyItems) { item ->
                    HourlyCard(
                        item = item,
                        hourLabel = item.dt.toHourLabel(locale),
                        iconRes = iconRes(item.weather.firstOrNull()?.icon ?: "01d"),
                        units = units
                    )
                }
            }
        }

        Spacer(Modifier.height(28.dp))

        SectionTitle(normal =stringResource(R.string.daily_bold), bold = stringResource(R.string.details))
        Spacer(Modifier.height(12.dp))

        DailyDetailsCard(
            pressure = current.main.pressure,
            windSpeed = current.wind.speed,
            humidity = current.main.humidity,
            clouds = current.clouds.all,
            windUnit = windUnit,
            lang = lang
        )

        Spacer(Modifier.height(28.dp))

        SectionTitle(normal =stringResource(R.string.next_days_normal), bold = stringResource(R.string.next_days_bold))
        Spacer(Modifier.height(12.dp))

        if (dailyItems.isEmpty()) {
            NoDataText(stringResource(R.string.no_forecast_data))
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                dailyItems.forEachIndexed { index, item ->
                    DailyRow(
                        item = item,
                        isToday = index == 0,
                        weekday = item.dt.toWeekday(locale),
                        shortDate = item.dt.toShortDate(locale),
                        iconRes = iconRes(item.weather.firstOrNull()?.icon ?: "01d"),
                        units = units,
                        lang = lang
                    )
                }
            }
        }

        Spacer(Modifier.height(80.dp))
    }
}

@Composable
private fun LoadingScreen() {
    val colors = AppTheme.colors
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = colors.accent)
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
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}

@Composable
private fun NoDataText(message: String) {
    Text(text = message, color = AppTheme.colors.textMuted, fontSize = 13.sp)
}

private fun Long.toTimeString(locale: Locale): String =
    SimpleDateFormat("hh:mm a", locale).format(Date(this * 1000L))

private fun Long.toDateString(locale: Locale): String =
    SimpleDateFormat("EEE, dd MMM", locale).format(Date(this * 1000L))

private fun Long.toHourLabel(locale: Locale): String =
    SimpleDateFormat("hha", locale).format(Date(this * 1000L)).uppercase(locale)

private fun Long.toWeekday(locale: Locale): String =
    SimpleDateFormat("EEEE", locale).format(Date(this * 1000L))

private fun Long.toShortDate(locale: Locale): String =
    SimpleDateFormat("EEE, dd MMM", locale).format(Date(this * 1000L))

private fun ForecastResponse.todayHourlyItems(): List<ForecastItem> {
    val today = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Date())
    return list.filter { it.dtTxt.startsWith(today) }
}

private fun ForecastResponse.dailyItems(): List<ForecastItem> {
    val byDay = list.groupBy { item ->
        SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Date(item.dt * 1000L))
    }
    return byDay.values.map { slots ->
        slots.firstOrNull { it.dtTxt.contains("12:00:00") } ?: slots.first()
    }.sortedBy { it.dt }
}


