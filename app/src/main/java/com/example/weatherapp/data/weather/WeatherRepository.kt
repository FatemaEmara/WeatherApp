package com.example.weatherapp.data.weather


import com.example.weatherapp.data.weather.datasource.local.FavoriteDao
import com.example.weatherapp.data.weather.datasource.remote.WeatherRemoteDataSource
import com.example.weatherapp.data.weather.model.CurrentWeatherResponse
import com.example.weatherapp.data.weather.model.FavoriteLocation
import com.example.weatherapp.data.weather.model.ForecastResponse
import com.example.weatherapp.utils.ResponseState
import kotlinx.coroutines.flow.Flow

class WeatherRepository(
    private val remoteDataSource: WeatherRemoteDataSource,
    private val favoriteDao: FavoriteDao
) {


    suspend fun getCurrentWeather(
        lat: Double,
        lon: Double
    ): Flow<ResponseState<CurrentWeatherResponse>> =
        remoteDataSource.getCurrentWeather(lat, lon)

    suspend fun getForecast(
        lat: Double,
        lon: Double
    ): Flow<ResponseState<ForecastResponse>> =
        remoteDataSource.getForecast(lat, lon)


    fun getAllFavorites(): Flow<List<FavoriteLocation>> =
        favoriteDao.getAllFavorites()

    suspend fun addFavorite(location: FavoriteLocation) =
        favoriteDao.insertFavorite(location)

    suspend fun removeFavorite(location: FavoriteLocation) =
        favoriteDao.deleteFavorite(location)

    suspend fun getFavoriteById(id: Int): FavoriteLocation? =
        favoriteDao.getFavoriteById(id)
}