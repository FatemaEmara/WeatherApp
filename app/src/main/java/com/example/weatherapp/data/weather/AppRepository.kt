package com.example.weatherapp.data.weather


import com.example.weatherapp.data.weather.model.CurrentWeatherResponse
import com.example.weatherapp.data.weather.model.FavoriteLocation
import com.example.weatherapp.data.weather.model.ForecastResponse
import com.example.weatherapp.utils.ResponseState
import kotlinx.coroutines.flow.Flow

interface AppRepository {

    suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        units: String,
        lang: String
    ): Flow<ResponseState<CurrentWeatherResponse>>

    suspend fun getForecast(
        lat: Double,
        lon: Double,
        units: String,
        lang: String
    ): Flow<ResponseState<ForecastResponse>>

    fun getAllFavorites(): Flow<List<FavoriteLocation>>
    suspend fun addFavorite(location: FavoriteLocation)
    suspend fun removeFavorite(location: FavoriteLocation)
    suspend fun getFavoriteById(id: Int): FavoriteLocation?

    fun getUnits(): Flow<String>
    suspend fun setUnits(units: String)

    fun getWindUnit(): Flow<String>
    suspend fun setWindUnit(unit: String)

    fun getLanguage(): Flow<String>
    suspend fun setLanguage(lang: String)

    fun getLocationMode(): Flow<String>
    suspend fun setLocationMode(mode: String)

    fun getManualLocation(): Flow<Pair<Double, Double>?>
    suspend fun setManualLocation(lat: Double, lon: Double)

    fun getThemeMode(): Flow<String>
    suspend fun setThemeMode(mode: String)
}