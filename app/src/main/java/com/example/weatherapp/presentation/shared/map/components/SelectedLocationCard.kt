package com.example.weatherapp.presentation.shared.map.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.ui.theme.AppTheme

@Composable
fun SelectedLocationCard(
    cityName: String,
    lat: Double,
    lon: Double,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors

    Column(modifier = modifier.fillMaxWidth()) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(colors.cardBg.copy(alpha = 0.95f))
                .border(1.dp, colors.cardBorder, RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Column {
                Text(
                    text = cityName.ifEmpty { "Selected location" },
                    color = colors.textPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "${String.format("%.4f", lat)}°N,  ${String.format("%.4f", lon)}°E",
                    color = colors.textMuted,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        Button(
            onClick = onSave,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.accent
            )
        ) {
            Text(
                text = "Save Location",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = colors.textPrimary
            )
        }
    }
}

@Composable
fun NoSelectionButton(modifier: Modifier = Modifier) {
    val colors = AppTheme.colors

    Button(
        onClick = {},
        enabled = false,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            disabledContainerColor = colors.cardBorder,
            disabledContentColor = colors.textMuted
        )
    ) {
        Text(
            text = "Select a location first",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}