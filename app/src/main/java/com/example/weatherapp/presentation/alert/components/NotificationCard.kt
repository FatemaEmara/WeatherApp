package com.example.weatherapp.presentation.alert.components

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.ui.theme.AppTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun NotificationCard(
    scheduledMillis: Long?,
    isScheduled: Boolean,
    onSchedule: (Long) -> Unit,
    onCancel: () -> Unit
) {
    val colors = AppTheme.colors
    val context = LocalContext.current
    val shape = RoundedCornerShape(20.dp)
    val fmt = SimpleDateFormat("hh:mm a, MMM dd", Locale.getDefault())

    fun openTimePicker() {
        val now = Calendar.getInstance()
        TimePickerDialog(
            context,
            { _, hour, minute ->
                val picked = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                    if (timeInMillis <= System.currentTimeMillis()) {
                        add(Calendar.DAY_OF_YEAR, 1)
                    }
                }
                onSchedule(picked.timeInMillis)
            },
            now.get(Calendar.HOUR_OF_DAY),
            now.get(Calendar.MINUTE),
            false
        ).show()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.cardBg, shape)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .background(colors.accent.copy(alpha = 0.15f), RoundedCornerShape(13.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.notifications),
                contentDescription = null,
                tint = colors.accent,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.alerts_notify_me_at),
                color = colors.textMuted,
                fontSize = 11.sp
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = scheduledMillis?.let { fmt.format(Date(it)) }
                    ?: stringResource(R.string.alerts_not_scheduled),
                color = if (isScheduled) colors.accent else colors.textPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            if (isScheduled) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = stringResource(R.string.alerts_fires_once),
                    color = colors.textMuted,
                    fontSize = 11.sp
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.End
        ) {
            OutlinedButton(
                onClick = { openTimePicker() },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.accent),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = if (isScheduled)  stringResource(R.string.alerts_change_time) else stringResource(R.string.alerts_set_time),
                    fontSize = 12.sp
                )
            }
            if (isScheduled) {
                OutlinedButton(
                    onClick = onCancel,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.danger),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text( stringResource(R.string.alerts_cancel), fontSize = 12.sp)
                }
            }
        }
    }
}