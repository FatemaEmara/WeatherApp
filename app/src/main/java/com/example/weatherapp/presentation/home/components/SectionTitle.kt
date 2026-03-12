package com.example.weatherapp.presentation.home.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.weatherapp.ui.theme.AppTheme

@Composable
fun SectionTitle(
    normal: String,
    bold: String,
    fontSize: TextUnit = 22.sp,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    Row(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "$normal ",
            color = colors.textPrimary,
            fontSize = fontSize,
            fontWeight = FontWeight.Normal
        )
        Text(
            text = bold,
            color = colors.textPrimary,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}