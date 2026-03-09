package com.example.weatherapp.data.weather.datasource.remote

import com.example.weatherapp.data.weather.model.CurrentWeatherResponse
import com.example.weatherapp.data.weather.model.ForecastResponse
import com.example.weatherapp.utils.ResponseState
import kotlinx.coroutines.flow.Flow

interface WeatherRemoteDataSource {

    suspend fun getCurrentWeather(
        lat: Double,
        lon: Double
    ): Flow<ResponseState<CurrentWeatherResponse>>

    suspend fun getForecast(
        lat: Double, lon: Double
    ): Flow<ResponseState<ForecastResponse>>

}
