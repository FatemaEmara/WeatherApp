package com.example.weatherapp.presentation.forcastdetail.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.weatherapp.R
import com.example.weatherapp.data.weather.AppRepository
import com.example.weatherapp.data.weather.model.ForecastItem
import com.example.weatherapp.data.weather.model.ForecastResponse
import com.example.weatherapp.presentation.forcastdetail.components.DetailDailyRow
import com.example.weatherapp.presentation.forcastdetail.components.DetailHourlyCard
import com.example.weatherapp.presentation.forcastdetail.components.TodaySummaryCard
import com.example.weatherapp.presentation.forcastdetail.viewmodel.ForecastDetailFactory
import com.example.weatherapp.presentation.forcastdetail.viewmodel.ForecastDetailViewModel
import com.example.weatherapp.presentation.home.components.SectionTitle
import com.example.weatherapp.ui.theme.AppTheme
import com.example.weatherapp.utils.ResponseState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ForecastDetailScreen(
    locationId: Int,
    navController: NavController,
    repository: AppRepository,
    viewModel: ForecastDetailViewModel
) {
    val colors = AppTheme.colors

    val viewModel: ForecastDetailViewModel = viewModel(
        key = "forecast_detail_$locationId",
        factory = ForecastDetailFactory(repository, locationId)
    )

    val location by viewModel.location.collectAsState()
    val forecastState by viewModel.forecastState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(colors.bgTop, colors.bgBottom)))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = colors.textPrimary
                    )
                }
                Spacer(Modifier.width(8.dp))
                Column {

                    Text(
                        text = location?.cityName ?: "Loading…",
                        color = colors.textPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (!location?.countryCode.isNullOrEmpty()) {
                        Text(
                            text = location!!.countryCode,
                            color = colors.textMuted,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            when (val state = forecastState) {
                is ResponseState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = colors.accent)
                    }
                }

                is ResponseState.Failure -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = state.error,
                            color = colors.textPrimary,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                is ResponseState.Success -> {
                    ForecastDetailContent(forecast = state.data, viewModel)
                }
            }
        }
    }
}

@Composable
private fun ForecastDetailContent(forecast: ForecastResponse, viewModel: ForecastDetailViewModel) {
    val colors = AppTheme.colors
    val hourlyItems = forecast.todayHourlyItems()
    val dailyItems = forecast.dailyItems()

    val units by viewModel.units.collectAsState()
    val windUnit by viewModel.windUnit.collectAsState()
    val lang by viewModel.lang.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(Modifier.height(4.dp))
            dailyItems.firstOrNull()?.let { TodaySummaryCard(it) }
        }

        item {
            SectionTitle(
                normal = stringResource(R.string.daily_normal),
                bold = stringResource(R.string.daily_bold)
            )
            Spacer(Modifier.height(10.dp))
            if (hourlyItems.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_hourly_data),
                    color = colors.textMuted,
                    fontSize = 13.sp
                )
            } else {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(hourlyItems) { item ->
                        DetailHourlyCard(
                            item = item,
                            hourLabel = item.dt.toHourLabel()
                        )
                    }
                }
            }
        }

        item {
            Spacer(Modifier.height(4.dp))
            SectionTitle(
                normal = stringResource(R.string.next_days_normal),
                bold = stringResource(R.string.forecast)
            )
            Spacer(Modifier.height(10.dp))
        }

        items(dailyItems) { item ->
            DetailDailyRow(
                item = item,
                weekday = item.dt.toWeekday(),
                shortDate = item.dt.toShortDate(),
                units = units
            )
        }

        item { Spacer(Modifier.height(80.dp)) }
    }
}

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