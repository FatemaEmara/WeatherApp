package com.example.weatherapp.data.weather

import com.example.weatherapp.data.weather.datasource.remote.WeatherRemoteDataSource
import com.example.weatherapp.data.weather.model.CurrentWeatherResponse
import com.example.weatherapp.data.weather.model.ForecastResponse
import com.example.weatherapp.utils.ResponseState
import kotlinx.coroutines.flow.Flow

class WeatherRepository(
    private val remoteDataSource: WeatherRemoteDataSource
) {

    suspend fun getCurrentWeather(
        lat: Double, lon: Double
    ): Flow<ResponseState<CurrentWeatherResponse>> {
        return remoteDataSource.getCurrentWeather(lat, lon)
    }

    suspend fun getForecast(
        lat: Double, lon: Double
    ): Flow<ResponseState<ForecastResponse>> {
        return remoteDataSource.getForecast(lat, lon)
    }
}