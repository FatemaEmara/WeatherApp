package com.example.weatherapp.presentation.home.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.data.weather.model.ForecastItem
import com.example.weatherapp.ui.theme.AppTheme
import kotlin.math.roundToInt

@Composable
fun HourlyCard(
    item: ForecastItem,
    hourLabel: String,
    iconRes: Int,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors

    WeatherCard(modifier = modifier.width(82.dp)) {
        Column(
            modifier = Modifier.padding(vertical = 14.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = hourLabel,
                color = colors.textMuted,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Image(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier.size(28.dp)
            )
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = "${item.main.temp.roundToInt()}",
                    color = colors.textPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "°C",
                    color = colors.textMuted,
                    fontSize = 10.sp
                )
            }
        }
    }
}