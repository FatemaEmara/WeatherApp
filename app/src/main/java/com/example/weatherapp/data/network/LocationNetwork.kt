package com.example.weatherapp.data.network


import com.example.weatherapp.data.weather.datasource.remote.LocationService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object LocationNetwork {

    val service: LocationService by lazy {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("User-Agent", "WeatherApp/1.0")
                    .build()
                chain.proceed(request)
            }
            .build()

        Retrofit.Builder()
            .baseUrl("https://nominatim.openstreetmap.org/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LocationService::class.java)
    }
}