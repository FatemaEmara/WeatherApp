package com.example.weatherapp.presentation.settings.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.weather.AppRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: AppRepository) : ViewModel() {


    val units = repository.getUnits().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), "metric"
    )

    val windUnit = repository.getWindUnit().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), "ms"
    )


    val language = repository.getLanguage().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), "en"
    )


    val locationMode = repository.getLocationMode().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), "gps"
    )

    val manualLocation = repository.getManualLocation().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), null
    )

    val themeMode = repository.getThemeMode().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), "system"
    )

    fun setUnits(units: String) = viewModelScope.launch {
        Log.d("SettingsVM", "setUnits called: $units")
        repository.setUnits(units)
    }

    fun setWindUnit(unit: String) = viewModelScope.launch { repository.setWindUnit(unit) }
    fun setLanguage(lang: String) = viewModelScope.launch { repository.setLanguage(lang) }
    fun setLocationMode(mode: String) =
        viewModelScope.launch { repository.setLocationMode(mode) }

    fun setThemeMode(mode: String) = viewModelScope.launch { repository.setThemeMode(mode) }

    fun saveManualLocation(lat: Double, lon: Double) =
        viewModelScope.launch { repository.setManualLocation(lat, lon) }
}

class SettingsFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}