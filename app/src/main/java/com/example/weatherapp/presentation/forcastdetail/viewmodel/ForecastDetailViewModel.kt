package com.example.weatherapp.presentation.forcastdetail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.weather.WeatherRepository
import com.example.weatherapp.data.weather.model.FavoriteLocation
import com.example.weatherapp.data.weather.model.ForecastResponse
import com.example.weatherapp.utils.ResponseState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ForecastDetailViewModel(
    private val repository: WeatherRepository,
    private val locationId: Int
) : ViewModel() {

    private val _location = MutableStateFlow<FavoriteLocation?>(null)
    val location: StateFlow<FavoriteLocation?> = _location.asStateFlow()

    private val _forecastState =
        MutableStateFlow<ResponseState<ForecastResponse>>(ResponseState.Loading())
    val forecastState: StateFlow<ResponseState<ForecastResponse>> =
        _forecastState.asStateFlow()

    init {
        loadLocationThenForecast()
    }

    private fun loadLocationThenForecast() {
        viewModelScope.launch {
            val loc = repository.getFavoriteById(locationId)
            _location.value = loc

            if (loc != null) {
                repository.getForecast(loc.lat, loc.lon).collect { result ->
                    _forecastState.value = result
                }
            } else {
                _forecastState.value = ResponseState.Failure("Location not found")
            }
        }
    }
}

class ForecastDetailFactory(
    private val repository: WeatherRepository,
    private val locationId: Int
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ForecastDetailViewModel::class.java)) {
            return ForecastDetailViewModel(repository, locationId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}