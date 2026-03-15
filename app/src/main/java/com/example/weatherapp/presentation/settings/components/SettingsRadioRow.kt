package com.example.weatherapp.presentation.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.ui.theme.AppTheme

@Composable
fun SettingsRadioRow(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    iconRes: Int? = null,
    showDivider: Boolean = true,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (iconRes != null) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    tint = if (selected) colors.accent else colors.textMuted,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(12.dp))
            }


            Text(
                text = label,
                color = if (selected) colors.textPrimary else colors.textMuted,
                fontSize = 15.sp,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                modifier = Modifier.weight(1f)
            )


            RadioButton(
                selected = selected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = colors.accent,
                    unselectedColor = colors.textMuted
                )
            )
        }

        if (showDivider) {
            HorizontalDivider(
                color = colors.cardBorder,
                thickness = 0.5.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}