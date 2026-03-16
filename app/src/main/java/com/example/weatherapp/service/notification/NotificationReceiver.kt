package com.example.weatherapp.service.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class NotificationReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {


        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .addTag("weather_one_time_notification")
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)

    }
}