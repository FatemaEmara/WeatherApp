package com.example.weatherapp.presentation.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
fun LocationSourceCard(
    selectedMode: String,
    manualLocation: Pair<Double, Double>?,
    onSelectGps: () -> Unit,
    onSelectMap: () -> Unit,
    onOpenMap: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors

    Column(modifier = modifier) {

        SettingsRadioRow(
            label = stringResource(R.string.use_gps),
            selected = selectedMode == "gps",
            onClick = onSelectGps,
            iconRes = R.drawable.gpsfixed,
            showDivider = true
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onSelectMap)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.location),
                contentDescription = null,
                tint = if (selectedMode == stringResource(R.string.map)) colors.accent else colors.textMuted,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.choose_from_map),
                    color = if (selectedMode == stringResource(R.string.map)) colors.textPrimary else colors.textMuted,
                    fontSize = 15.sp,
                    fontWeight = if (selectedMode == stringResource(R.string.map)) FontWeight.SemiBold else FontWeight.Normal
                )
                if (selectedMode == stringResource(R.string.map) && manualLocation != null) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "${String.format("%.2f", manualLocation.first)}°N  " +
                                "${String.format("%.2f", manualLocation.second)}°E",
                        color = colors.accent,
                        fontSize = 12.sp
                    )
                }
            }
            RadioButton(
                selected = selectedMode == stringResource(R.string.map),
                onClick = onSelectMap,
                colors = RadioButtonDefaults.colors(
                    selectedColor = colors.accent,
                    unselectedColor = colors.textMuted
                )
            )
        }

        if (selectedMode == stringResource(R.string.map)) {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Button(
                    onClick = onOpenMap,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colors.accent)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.location),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))

                    Text(
                        text = if (manualLocation != null) stringResource(R.string.change_location)
                        else stringResource(R.string.open_map),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
        }
    }
}