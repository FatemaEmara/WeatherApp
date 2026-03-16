package com.example.weatherapp.presentation.alert.components

import androidx.compose.foundation.layout.*
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

@Composable
fun EmptyAlertsState() {
    val colors = AppTheme.colors
    Box(
        modifier         = Modifier
            .fillMaxWidth()
            .padding(vertical = 36.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
         Icon(
             painter = painterResource(R.drawable.alerts),
             contentDescription = "no alerts",
             modifier = Modifier.size(52.dp)
         )
            Spacer(Modifier.height(4.dp))
            Text(
                text       = stringResource(R.string.alerts_empty_title),
                color      = colors.textPrimary,
                fontSize   = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text     = stringResource(R.string.alerts_empty_subtitle),
                color    = colors.textMuted,
                fontSize = 13.sp
            )
        }
    }
}