package com.example.weatherapp.service.alert

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build

object AlertScheduler {


    const val ACTION_ALERT_START = "ACTION_ALERT_START"
    const val ACTION_ALERT_END = "ACTION_ALERT_END"
    const val EXTRA_ALERT_ID = "alertId"
    const val EXTRA_ALERT_TYPE = "alertType"
    const val EXTRA_WEATHER_TYPE = "weatherType"

    fun schedule(
        context: Context,
        alertId: Int,
        startTime: Long,
        endTime: Long,
        alertType: String,
        weatherType: String
    ) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val canSchedule = context.getSystemService(AlarmManager::class.java)
                .canScheduleExactAlarms()
        }

        val alarmManager = context.getSystemService(AlarmManager::class.java)

        val startIntent = Intent(context, AlertReceiver::class.java).apply {
            action = ACTION_ALERT_START
            putExtra(EXTRA_ALERT_ID, alertId)
            putExtra(EXTRA_ALERT_TYPE, alertType)
            putExtra(EXTRA_WEATHER_TYPE, weatherType)
        }
        val startPending = PendingIntent.getBroadcast(
            context, alertId, startIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val endIntent = Intent(context, AlertReceiver::class.java).apply {
            action = ACTION_ALERT_END
            putExtra(EXTRA_ALERT_ID, alertId)
        }
        val endPending = PendingIntent.getBroadcast(
            context, alertId + 10000, endIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        scheduleExact(alarmManager, startTime, startPending, "start alertId=$alertId")
        scheduleExact(alarmManager, endTime, endPending, "end alertId=$alertId")
    }

    fun cancel(context: Context, alertId: Int) {
        val alarmManager = context.getSystemService(AlarmManager::class.java)

        val startPending = PendingIntent.getBroadcast(
            context, alertId,
            Intent(context, AlertReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val endPending = PendingIntent.getBroadcast(
            context, alertId + 10000,
            Intent(context, AlertReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(startPending)
        alarmManager.cancel(endPending)
    }

    private fun scheduleExact(
        alarmManager: AlarmManager,
        triggerMillis: Long,
        pendingIntent: PendingIntent,
        label: String
    ) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP, triggerMillis, pendingIntent
                    )
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, triggerMillis, pendingIntent)
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP, triggerMillis, pendingIntent
                )
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerMillis, pendingIntent)
            }
        } catch (e: SecurityException) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerMillis, pendingIntent)
        }
    }
}