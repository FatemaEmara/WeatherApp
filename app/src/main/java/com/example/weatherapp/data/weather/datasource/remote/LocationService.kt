package com.example.weatherapp.data.weather.datasource.remote

import com.example.weatherapp.data.weather.model.LocationResult
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationService {

    @GET("search")
    suspend fun searchCity(
        @Query("q")              query: String,
        @Query("format")         format: String   = "json",
        @Query("limit")          limit: Int       = 5,
        @Query("addressdetails") addressDetails: Int = 1,
        @Query("accept-language") lang: String    = "en"
    ): List<LocationResult>

    @GET("reverse")
    suspend fun reverseGeocode(
        @Query("lat")            lat: Double,
        @Query("lon")            lon: Double,
        @Query("format")         format: String   = "json",
        @Query("addressdetails") addressDetails: Int = 1,
        @Query("accept-language") lang: String    = "en"
    ): LocationResult
}