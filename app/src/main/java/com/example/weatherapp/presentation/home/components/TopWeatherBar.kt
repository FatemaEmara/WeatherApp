package com.example.weatherapp.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.ui.theme.AppTheme
import kotlin.math.roundToInt

@Composable
fun TopWeatherBar(
    description: String,
    feelsLike: Double,
    dateString: String,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {

        Column {
            Text(
                text = description,
                color = colors.textPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.thermometer),
                    contentDescription = "Feels like",
                    tint = colors.accent,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "Feels like ${feelsLike.roundToInt()}°",
                    color = colors.textMuted,
                    fontSize = 14.sp
                )
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "Today",
                color = colors.textPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = dateString,
                color = colors.textMuted,
                fontSize = 13.sp
            )
        }
    }
}