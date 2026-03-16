package com.example.weatherapp.presentation.alert.view

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherapp.data.weather.model.Alert
import com.example.weatherapp.presentation.alert.components.AddAlertBottomSheet
import com.example.weatherapp.presentation.alert.components.AlertCard
import com.example.weatherapp.presentation.alert.components.EmptyAlertsState
import com.example.weatherapp.presentation.alert.components.NotificationCard
import com.example.weatherapp.presentation.alert.components.PermissionWarningBanner
import com.example.weatherapp.presentation.alert.components.SectionLabel
import com.example.weatherapp.presentation.alert.viewmodel.AlertsViewModel
import com.example.weatherapp.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun AlertsScreen(viewModel: AlertsViewModel) {

    val colors = AppTheme.colors
    val alerts: List<Alert> by viewModel.alerts.collectAsStateWithLifecycle()
    var showAddSheet by remember { mutableStateOf(false) }
    var notificationTimeMillis by remember { mutableStateOf<Long?>(null) }
    var notificationScheduled by remember { mutableStateOf(false) }
    var hasNotifPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) false else true
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasNotifPermission = granted }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(colors.bgTop, colors.bgBottom)))
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(top = 24.dp, bottom = 100.dp)
        ) {

            item {
                Row {
                    Text(
                        text = stringResource(com.example.weatherapp.R.string.alerts_title_normal),
                        color = colors.textPrimary,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Light
                    )
                    Text(
                        text = stringResource(com.example.weatherapp.R.string.alerts_title_bold),
                        color = colors.textPrimary,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.height(2.dp))
                val active_alarm = stringResource(com.example.weatherapp.R.string.alerts_title_bold)

                Text(
                    text = "${alerts.size} $active_alarm ${if (alerts.size != 1) "s" else ""}",
                    color = colors.textMuted,
                    fontSize = 13.sp
                )
            }

            if (!hasNotifPermission) {
                item {
                    PermissionWarningBanner(
                        onRequestAgain = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            }
                        }
                    )
                }
            }

            item {
                SectionLabel(
                    title = stringResource(com.example.weatherapp.R.string.alerts_section_notification),
                    subtitle = stringResource(com.example.weatherapp.R.string.alerts_section_notification_subtitle)
                )
                Spacer(Modifier.height(10.dp))
                NotificationCard(
                    scheduledMillis = notificationTimeMillis,
                    isScheduled = notificationScheduled,
                    onSchedule = { millis ->
                        notificationTimeMillis = millis
                        notificationScheduled = true
                        viewModel.scheduleNotification(millis)
                    },
                    onCancel = {
                        viewModel.cancelNotification()
                        notificationTimeMillis = null
                        notificationScheduled = false
                    }
                )
            }

            item {
                Spacer(Modifier.height(4.dp))
                SectionLabel(
                    title = stringResource(com.example.weatherapp.R.string.alerts_section_alarms),
                    subtitle = stringResource(com.example.weatherapp.R.string.alerts_section_alarms_subtitle)
                )
                Spacer(Modifier.height(10.dp))
            }

            if (alerts.isEmpty()) {
                item { EmptyAlertsState() }
            } else {
                items(alerts, key = { it.id }) { alert ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + slideInVertically { it / 2 },
                        exit = fadeOut() + slideOutVertically { it / 2 }
                    ) {
                        AlertCard(
                            alert = alert,
                            onStop = { viewModel.stopAlert(alert) },
                            onDelete = { viewModel.deleteAlert(alert) }
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showAddSheet = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 24.dp),
            containerColor = colors.accent,
            contentColor = colors.textPrimary,
            shape = CircleShape
        ) {
            Icon(
                Icons.Filled.Add,
                contentDescription = stringResource(com.example.weatherapp.R.string.alerts_new_alarm)
            )

        }


    }

    if (showAddSheet) {
        AddAlertBottomSheet(
            onDismiss = { showAddSheet = false },
            onConfirm = { alert ->
                viewModel.addAlert(alert)
                showAddSheet = false
            }
        )
    }
}