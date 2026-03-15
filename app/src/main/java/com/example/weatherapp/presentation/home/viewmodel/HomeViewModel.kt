package com.example.weatherapp.presentation.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.weather.AppRepository
import com.example.weatherapp.data.weather.model.CurrentWeatherResponse
import com.example.weatherapp.data.weather.model.ForecastResponse
import com.example.weatherapp.utils.ResponseState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: AppRepository) : ViewModel() {

    private val _weatherState =
        MutableStateFlow<ResponseState<CurrentWeatherResponse>>(ResponseState.Loading())
    val weatherState: StateFlow<ResponseState<CurrentWeatherResponse>> =
        _weatherState.asStateFlow()

    private val _forecastState =
        MutableStateFlow<ResponseState<ForecastResponse>>(ResponseState.Loading())
    val forecastState: StateFlow<ResponseState<ForecastResponse>> =
        _forecastState.asStateFlow()

    val units = repository.getUnits().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), "metric"
    )
    val windUnit = repository.getWindUnit().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), "ms"
    )
    val lang = repository.getLanguage().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), "en"
    )


    val locationMode = repository.getLocationMode().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), "gps"
    )

    private var lastLat: Double? = null
    private var lastLon: Double? = null

    init {

        viewModelScope.launch {
            val mode = repository.getLocationMode().first()
            if (mode == "map") {
                val location = repository.getManualLocation().first()
                if (location != null) {
                    Log.d("HomeVM", "Map mode: loading from saved location $location")
                    loadWeather(location.first, location.second)
                } else {
                    Log.d("HomeVM", "Map mode but no location saved yet")
                    _weatherState.value = ResponseState.Failure(
                        "No location selected. Please choose a location from Settings."
                    )
                }
            }
        }

        combine(
            repository.getUnits(),
            repository.getLanguage()
        ) { units, lang -> units to lang }
            .onEach { (units, lang) ->
                Log.d("HomeVM", "Settings changed: units=$units lang=$lang")
                val lat = lastLat ?: return@onEach
                val lon = lastLon ?: return@onEach
                fetchWeather(lat, lon)
            }
            .launchIn(viewModelScope)

        combine(
            repository.getLocationMode(),
            repository.getManualLocation()
        ) { mode, loc -> mode to loc }
            .onEach { (mode, location) ->
                Log.d("HomeVM", "Location setting changed: mode=$mode location=$location")
                if (mode == "map" && location != null) {
                    loadWeather(location.first, location.second)
                }
            }
            .launchIn(viewModelScope)
    }

    fun loadWeather(lat: Double, lon: Double) {
        Log.d("HomeVM", "loadWeather called: lat=$lat lon=$lon")
        lastLat = lat
        lastLon = lon
        fetchWeather(lat, lon)
    }

    private fun fetchWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            val units = repository.getUnits().first()
            val lang = repository.getLanguage().first()
            Log.d("HomeVM", "fetchWeather: lat=$lat lon=$lon units=$units lang=$lang")
            launch {
                repository.getCurrentWeather(lat, lon, units, lang)
                    .collect { _weatherState.value = it }
            }
            launch {
                repository.getForecast(lat, lon, units, lang)
                    .collect { _forecastState.value = it }
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class HomeFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}










