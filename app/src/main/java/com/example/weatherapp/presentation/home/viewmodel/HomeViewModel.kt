package com.example.weatherapp.presentation.home.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.weather.WeatherRepository
import com.example.weatherapp.data.weather.model.CurrentWeatherResponse
import com.example.weatherapp.data.weather.model.ForecastResponse
import com.example.weatherapp.utils.ResponseState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {
    private val _weatherState =
        MutableStateFlow<ResponseState<CurrentWeatherResponse>>(ResponseState.Loading())

    val weatherState: StateFlow<ResponseState<CurrentWeatherResponse>> =
        _weatherState.asStateFlow()

    private val _forecastState =
        MutableStateFlow<ResponseState<ForecastResponse>>(ResponseState.Loading())
    val forecastState: StateFlow<ResponseState<ForecastResponse>> =
        _forecastState.asStateFlow()

    fun loadCurrentWeather(lat: Double, lon: Double) {

        viewModelScope.launch {

            weatherRepository.getCurrentWeather(lat, lon)
                .collect {

                    _weatherState.value = it

                }
        }
    }

    fun loadForecast(lat: Double, lon: Double) {
        viewModelScope.launch {
            weatherRepository.getForecast(lat, lon).collect {
                _forecastState.value = it
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class HomeFactory(private val weatherRepository: WeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(weatherRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}