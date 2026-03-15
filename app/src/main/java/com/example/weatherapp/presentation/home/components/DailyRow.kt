package com.example.weatherapp.presentation.home.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.example.weatherapp.data.weather.model.ForecastItem
import com.example.weatherapp.ui.theme.AppTheme
import kotlin.math.roundToInt

@Composable
fun DailyRow(
    item: ForecastItem,
    isToday: Boolean,
    weekday: String,
    shortDate: String,
    iconRes: Int,
    units: String,
    lang: String,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    val unitLabel = when (units) {
        "imperial" -> "°F"
        "standard" -> "K"
        else       -> "°C"
    }
    WeatherCard(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {
                val temp = stringResource(R.string.today)
                Text(
                    text = if (isToday) temp else weekday,
                    color = colors.textPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = shortDate,
                    color = colors.textMuted,
                    fontSize = 12.sp
                )
            }


            Image(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier
                    .size(36.dp)
                    .padding(horizontal = 4.dp)
            )

            Spacer(Modifier.width(8.dp))


            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.Top) {
                    Text(
                        text = "${item.main.tempMax.roundToInt()}°",
                        color = colors.textPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = " / ",
                        color = colors.textMuted,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "${item.main.tempMin.roundToInt()} $unitLabel",
                        color = colors.textMuted,
                        fontSize = 14.sp
                    )
                }
                val temp2 = stringResource(R.string.feels_like)
                Text(
                    text = "$temp2 ${item.main.feelsLike.roundToInt()} $unitLabel",
                    color = colors.textMuted,
                    fontSize = 11.sp
                )
            }
        }
    }
}