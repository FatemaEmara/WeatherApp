package com.example.weatherapp.presentation.alert.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.weatherapp.data.weather.model.Alert
import com.example.weatherapp.ui.theme.AppTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AlertCard(
    alert: Alert,
    onStop: () -> Unit,
    onDelete: () -> Unit
) {
    val colors      = AppTheme.colors
    val fmt         = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault())
    val shape       = RoundedCornerShape(16.dp)

    val weatherIcon  = weatherIcon(alert.weatherType)
    val weatherTitle = weatherLabel(alert.weatherType)

    Card(
        shape    = shape,
        colors   = CardDefaults.cardColors(containerColor = colors.cardBg),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Icon(
                painter            = painterResource(weatherIcon),
                contentDescription = weatherTitle,
                tint               = colors.textPrimary,
                modifier           = Modifier.size(36.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = weatherTitle,
                    color      = colors.textPrimary,
                    fontSize   = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text     = stringResource(R.string.alerts_card_from, fmt.format(Date(alert.startTime))),
                    color    = colors.textMuted,
                    fontSize = 12.sp
                )
                Text(
                    text     = stringResource(R.string.alerts_card_until, fmt.format(Date(alert.endTime))),
                    color    = colors.textMuted,
                    fontSize = 12.sp
                )
                if (alert.isActive) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text          = stringResource(R.string.alerts_card_active),
                        color         = colors.danger,
                        fontSize      = 11.sp,
                        fontWeight    = FontWeight.Medium,
                        letterSpacing = 0.3.sp
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (alert.isActive) {
                    IconButton(
                        onClick  = onStop,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            painter            = painterResource(R.drawable.stop),
                            contentDescription = stringResource(R.string.alerts_stop_alarm),
                            tint               = colors.danger,
                            modifier           = Modifier.size(22.dp)
                        )
                    }
                }
                IconButton(
                    onClick  = onDelete,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        painter            = painterResource(R.drawable.delete),
                        contentDescription = stringResource(R.string.alerts_delete_alert),
                        tint               = colors.textMuted,
                        modifier           = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}

private fun weatherIcon(type: String): Int = when (type) {
    "rain"  -> R.drawable.rainy
    "snow"  -> R.drawable.snow
    "storm" -> R.drawable.storm
    "fog"   -> R.drawable.fog
    "heat"  -> R.drawable.heat
    "cold"  -> R.drawable.cold
    "wind"  -> R.drawable.air
    "any"   -> R.drawable.sun
    else    -> R.drawable.sun
}

@Composable
private fun weatherLabel(type: String): String = stringResource(
    when (type) {
        "rain"  -> R.string.weather_type_rain
        "snow"  -> R.string.weather_type_snow
        "storm" -> R.string.weather_type_storm
        "fog"   -> R.string.weather_type_fog
        "heat"  -> R.string.weather_type_heat
        "cold"  -> R.string.weather_type_cold
        "wind"  -> R.string.weather_type_wind
        "any"   -> R.string.weather_type_any
        else    -> R.string.weather_type_unknown
    }
)