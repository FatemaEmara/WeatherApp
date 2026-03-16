package com.example.weatherapp.presentation.alert.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.ui.theme.AppTheme

@Composable
fun SectionLabel(title: String, subtitle: String = "") {
    val colors = AppTheme.colors
    Column {
        Text(
            text = title.uppercase(),
            color = colors.accent,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp
        )
        if (subtitle.isNotEmpty()) {
            Spacer(Modifier.height(2.dp))
            Text(
                text = subtitle,
                color = colors.textMuted,
                fontSize = 12.sp
            )
        }
    }
}