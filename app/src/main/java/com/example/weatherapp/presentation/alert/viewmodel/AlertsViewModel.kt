package com.example.weatherapp.presentation.alert.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.weather.AppRepository
import com.example.weatherapp.data.weather.model.Alert
import com.example.weatherapp.service.alert.AlertScheduler
import com.example.weatherapp.service.notification.NotificationScheduler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AlertsViewModel(
    private val repository: AppRepository,
    private val context: Context
) : ViewModel() {

    val alerts: StateFlow<List<Alert>> = repository.getAllAlerts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun scheduleNotification(timeInMillis: Long) {
        NotificationScheduler.schedule(context, timeInMillis)
    }

    fun cancelNotification() {
        NotificationScheduler.cancel(context)
    }

    fun addAlert(alert: Alert) {
        viewModelScope.launch {
            val generatedId = repository.insertAlert(alert)
            val alertWithId = alert.copy(id = generatedId.toInt())

            AlertScheduler.schedule(
                context = context,
                alertId = alertWithId.id,
                startTime = alertWithId.startTime,
                endTime = alertWithId.endTime,
                alertType = alertWithId.type,
                weatherType = alertWithId.weatherType
            )
        }
    }

    fun deleteAlert(alert: Alert) {
        viewModelScope.launch {
            AlertScheduler.cancel(context, alert.id)
            repository.deleteAlert(alert)
        }
    }

    fun stopAlert(alert: Alert) {
        viewModelScope.launch {
            AlertScheduler.cancel(context, alert.id)
            repository.deactivateAlert(alert.id)
        }
    }
}

class AlertsFactory(
    private val repository: AppRepository,
    private val context: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlertsViewModel::class.java)) {
            return AlertsViewModel(repository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}