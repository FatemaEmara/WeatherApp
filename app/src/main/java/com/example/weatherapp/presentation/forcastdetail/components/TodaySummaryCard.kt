package com.example.weatherapp.presentation.forcastdetail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.data.weather.model.ForecastItem
import com.example.weatherapp.presentation.shared.iconRes
import com.example.weatherapp.ui.theme.AppTheme
import kotlin.math.roundToInt

@Composable
fun TodaySummaryCard(
    item: ForecastItem,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    val shape = RoundedCornerShape(24.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(colors.cardBg)
            .border(1.dp, colors.cardBorder, shape)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = item.weather.firstOrNull()?.description
                        ?.replaceFirstChar { it.uppercase() } ?: "—",
                    color = colors.textMuted,
                    fontSize = 14.sp
                )
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.Top) {
                    Text(
                        text = "${item.main.temp.roundToInt()}",
                        color = colors.textPrimary,
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Thin
                    )
                    Text(
                        text = "°C",
                        color = colors.textMuted,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.thermometer),
                        contentDescription = null,
                        tint = colors.accent,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "Feels like ${item.main.feelsLike.roundToInt()}°C",
                        color = colors.textMuted,
                        fontSize = 13.sp
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(
                        iconRes(item.weather.firstOrNull()?.icon ?: "01d")
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(72.dp)
                )
                Spacer(Modifier.height(10.dp))
                Row {
                    DetailBadge(
                        iconRes = R.drawable.waterdrop,
                        value = "${item.main.humidity}%"
                    )
                    Spacer(Modifier.width(8.dp))
                    DetailBadge(iconRes = R.drawable.air, value = "${item.wind.speed}m/s")
                }
            }
        }
    }
}

@Composable
private fun DetailBadge(iconRes: Int, value: String) {
    val colors = AppTheme.colors
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(colors.cardBorder)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = colors.accent,
            modifier = Modifier.size(13.dp)
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = value,
            color = colors.textPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}