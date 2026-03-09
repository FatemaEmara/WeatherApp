package com.example.weatherapp.data.weather.datasource.remote

import com.example.weatherapp.data.weather.model.CurrentWeatherResponse
import com.example.weatherapp.data.weather.model.ForecastResponse
import com.example.weatherapp.utils.ResponseState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRemoteDataSourceImpl(
    private val weatherService: WeatherService
) : WeatherRemoteDataSource {

    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double
    ): Flow<ResponseState<CurrentWeatherResponse>> = flow {
        emit(ResponseState.Loading())
        try {
            val response = weatherService.getCurrentWeather(lat, lon)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    emit(ResponseState.Success(body))
                } else {
                    emit(ResponseState.Failure("Empty response body"))
                }
            } else {
                emit(ResponseState.Failure("Error ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(ResponseState.Failure(e.localizedMessage ?: "Unknown error"))
        }
    }

    override suspend fun getForecast(
        lat: Double,
        lon: Double
    ): Flow<ResponseState<ForecastResponse>> = flow {
        emit(ResponseState.Loading())
        try {
            val response = weatherService.getForecast(lat, lon)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    emit(ResponseState.Success(body))
                } else {
                    emit(ResponseState.Failure("Empty response body"))
                }
            } else {
                emit(ResponseState.Failure("Error ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(ResponseState.Failure(e.localizedMessage ?: "Unknown error"))
        }
    }}
