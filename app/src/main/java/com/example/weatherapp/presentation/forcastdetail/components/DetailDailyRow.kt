package com.example.weatherapp.presentation.forcastdetail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.data.weather.model.ForecastItem
import com.example.weatherapp.presentation.shared.iconRes
import com.example.weatherapp.ui.theme.AppTheme
import kotlin.math.roundToInt

@Composable
fun DetailDailyRow(
    item: ForecastItem,
    weekday: String,
    shortDate: String,
    units: String,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    val shape = RoundedCornerShape(18.dp)
    val unitLabel = when (units) {
        "imperial" -> "°F"
        "standard" -> "K"
        else -> "°C"
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(colors.cardBg)
            .border(1.dp, colors.cardBorder, shape)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = weekday,
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
            painter = painterResource(
                iconRes(item.weather.firstOrNull()?.icon ?: "01d")
            ),
            contentDescription = null,
            modifier = Modifier.size(36.dp)
        )

        Spacer(Modifier.width(12.dp))


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
                    fontSize = 13.sp
                )
                Text(
                    text = "${item.main.tempMin.roundToInt()} $unitLabel",
                    color = colors.textMuted,
                    fontSize = 13.sp
                )
            }
            val feels = stringResource(R.string.feels_like)
            Text(
                text = "$feels ${item.main.feelsLike.roundToInt()} $unitLabel",
                color = colors.textMuted,
                fontSize = 11.sp
            )
        }
    }
}