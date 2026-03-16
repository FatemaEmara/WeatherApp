package com.example.weatherapp

import android.app.Application
import com.example.weatherapp.service.alert.AlertReceiver
import com.example.weatherapp.service.notification.NotificationHelper

class WeatherApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createNotificationChannel(this)
        AlertReceiver.createAlertChannel(this)
    }
}