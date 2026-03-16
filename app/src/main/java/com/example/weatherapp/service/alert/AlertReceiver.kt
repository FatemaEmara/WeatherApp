package com.example.weatherapp.service.alert

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.weatherapp.R
import java.util.concurrent.TimeUnit

class AlertReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {

            AlertScheduler.ACTION_ALERT_START -> {

                val alertId = intent.getIntExtra(AlertScheduler.EXTRA_ALERT_ID, -1)
                val alertType =
                    intent.getStringExtra(AlertScheduler.EXTRA_ALERT_TYPE) ?: "notification"
                val weatherType = intent.getStringExtra(AlertScheduler.EXTRA_WEATHER_TYPE) ?: ""


                val inputData = Data.Builder()
                    .putInt(AlertScheduler.EXTRA_ALERT_ID, alertId)
                    .putString(AlertScheduler.EXTRA_ALERT_TYPE, alertType)
                    .putString(AlertScheduler.EXTRA_WEATHER_TYPE, weatherType)
                    .build()

                val workRequest = PeriodicWorkRequestBuilder<AlertWorker>(15, TimeUnit.MINUTES)
                    .setInputData(inputData)
                    .addTag("alert_worker_$alertId")
                    .build()

                WorkManager.getInstance(context)
                    .enqueueUniquePeriodicWork(
                        "alert_$alertId",
                        androidx.work.ExistingPeriodicWorkPolicy.REPLACE,
                        workRequest
                    )
            }

            AlertScheduler.ACTION_ALERT_END -> {
                val alertId = intent.getIntExtra(AlertScheduler.EXTRA_ALERT_ID, -1)

                WorkManager.getInstance(context).cancelUniqueWork("alert_$alertId")
                AlarmSoundHelper.stopAlarm()
            }

            ACTION_STOP_ALARM -> {
                val alertId = intent.getIntExtra(EXTRA_ALERT_ID, -1)

                AlarmSoundHelper.stopAlarm()

                val manager = context.getSystemService(NotificationManager::class.java)
                manager.cancel(alertId)

                WorkManager.getInstance(context).cancelUniqueWork("alert_$alertId")
            }

            else -> {
            }
        }
    }

    companion object {
        const val CHANNEL_ID = "weather_alert_channel"
        const val ACTION_STOP_ALARM = "ACTION_STOP_ALARM"
        const val EXTRA_ALERT_ID = "alertId"

        fun createAlertChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    "Weather Alerts",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Weather alert notifications with alarm sound"
                }
                context.getSystemService(NotificationManager::class.java)
                    .createNotificationChannel(channel)
            }
        }

        fun buildAlarmNotification(
            context: Context,
            alertId: Int,
            title: String,
            message: String
        ): android.app.Notification {
            val stopIntent = Intent(context, AlertReceiver::class.java).apply {
                action = ACTION_STOP_ALARM
                putExtra(EXTRA_ALERT_ID, alertId)
            }
            val stopPending = PendingIntent.getBroadcast(
                context,
                alertId + 20000,
                stopIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            return NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.notifications)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true)
                .setAutoCancel(false)
                .addAction(R.drawable.stop, "Stop Alarm", stopPending)
                .build()
        }
    }
}