package com.example.weatherapp.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.ui.theme.AppTheme

@Composable
fun DailyDetailsCard(
    pressure: Int,
    windSpeed: Double,
    humidity: Int,
    clouds: Int,
    windUnit: String,
    lang: String,
    modifier: Modifier = Modifier
) {

    val windLabel = when (windUnit) {
        "mph" -> "mph"
        else -> "m/s"
    }
    WeatherCard(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DetailItem(
                    iconRes = R.drawable.compress,
                    label = stringResource(R.string.pressure),
                    value = "$pressure hPa"
                )
                DetailItem(
                    iconRes = R.drawable.air,
                    label = stringResource(R.string.wind_speed),
                    value = "$windSpeed $windLabel"
                )
            }
            Spacer(Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DetailItem(
                    iconRes = R.drawable.waterdrop,
                    label = stringResource(R.string.humidity),
                    value = "$humidity %"
                )

                DetailItem(
                    iconRes = R.drawable.clouds,
                    label = stringResource(R.string.clouds),
                    value = "$clouds %"
                )
            }
        }
    }
}

@Composable
private fun DetailItem(
    iconRes: Int,
    label: String,
    value: String
) {
    val colors = AppTheme.colors

    Column(horizontalAlignment = Alignment.Start) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = label,
                tint = colors.accent,
                modifier = Modifier.size(14.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = label,
                color = colors.textMuted,
                fontSize = 12.sp
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = value,
            color = colors.textPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}