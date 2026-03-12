package com.example.weatherapp.data.weather.datasource.remote

import com.example.weatherapp.data.weather.model.CurrentWeatherResponse
import com.example.weatherapp.data.weather.model.ForecastResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {


    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "en",
        @Query("appid") apiKey: String = "862050ad9609e273069286c1c1154cc3"
    ): Response<CurrentWeatherResponse>


    @GET("forecast")
    suspend fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "en",
        @Query("appid") apiKey: String = "862050ad9609e273069286c1c1154cc3"
    ): Response<ForecastResponse>


}