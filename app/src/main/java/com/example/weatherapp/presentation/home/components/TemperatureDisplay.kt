package com.example.weatherapp.presentation.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import kotlin.math.roundToInt

@Composable
fun TemperatureDisplay(
    iconRes: Int,
    tempCelsius: Double,
    cityName: String,
    countryCode: String,
    units: String,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    val unitLabel = when (units) {
        "imperial" -> "°F"
        "standard" -> "K"
        else       -> "°C"
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(iconRes),
            contentDescription = null,
            modifier = Modifier.size(96.dp)
        )

        Spacer(Modifier.height(8.dp))


        Row(verticalAlignment = Alignment.Top) {
            Text(
                text = "${tempCelsius.roundToInt()}",
                color = colors.textPrimary,
                fontSize = 96.sp,
                fontWeight = FontWeight.Thin,
                letterSpacing = (-4).sp,
                lineHeight = 96.sp
            )
            Text(
                text     = unitLabel,
                color = colors.textMuted,
                fontSize = 22.sp,
                modifier = Modifier.padding(top = 18.dp)
            )
        }

        Spacer(Modifier.height(4.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.location),
                contentDescription = stringResource(R.string.section_location),
                tint = colors.accent,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = "$cityName, $countryCode",
                color = colors.textMuted,
                fontSize = 15.sp
            )
        }
    }
}