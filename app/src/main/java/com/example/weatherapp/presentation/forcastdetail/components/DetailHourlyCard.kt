package com.example.weatherapp.presentation.forcastdetail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.data.weather.model.ForecastItem
import com.example.weatherapp.presentation.shared.iconRes
import com.example.weatherapp.ui.theme.AppTheme
import kotlin.math.roundToInt

@Composable
fun DetailHourlyCard(
    item: ForecastItem,
    hourLabel: String,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    val shape = RoundedCornerShape(18.dp)

    Box(
        modifier = modifier
            .width(80.dp)
            .clip(shape)
            .background(colors.cardBg)
            .border(1.dp, colors.cardBorder, shape)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = hourLabel,
                color = colors.textMuted,
                fontSize = 11.sp
            )
            Image(
                painter = painterResource(
                    iconRes(item.weather.firstOrNull()?.icon ?: "01d")
                ),
                contentDescription = null,
                modifier = Modifier.size(28.dp)
            )
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = "${item.main.temp.roundToInt()}",
                    color = colors.textPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "°",
                    color = colors.textMuted,
                    fontSize = 11.sp
                )
            }
        }
    }
}