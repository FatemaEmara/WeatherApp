package com.example.weatherapp.data.network

import com.example.weatherapp.data.weather.datasource.remote.WeatherService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherNetwork {

    //https://api.openweathermap.org/data/2.5/weather?lat=44.34&lon=10.99&appid=862050ad9609e273069286c1c1154cc3
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val weatherService: WeatherService by lazy {
        retrofit.create(WeatherService::class.java)
    }
}

