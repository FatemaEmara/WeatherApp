package com.example.weatherapp.data.weather

import com.example.weatherapp.data.weather.datasource.local.LocalDataSource
import com.example.weatherapp.data.weather.datasource.remote.WeatherRemoteDataSource
import com.example.weatherapp.data.weather.model.CurrentWeatherResponse
import com.example.weatherapp.data.weather.model.FavoriteLocation
import com.example.weatherapp.data.weather.model.ForecastResponse
import com.example.weatherapp.utils.ResponseState
import kotlinx.coroutines.flow.Flow

class AppRepositoryImpl(
    private val remoteDataSource: WeatherRemoteDataSource,
    private val localDataSource: LocalDataSource
) : AppRepository {

    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        units: String,
        lang: String
    ): Flow<ResponseState<CurrentWeatherResponse>> =
        remoteDataSource.getCurrentWeather(lat, lon, units, lang)

    override suspend fun getForecast(
        lat: Double,
        lon: Double,
        units: String,
        lang: String

    ): Flow<ResponseState<ForecastResponse>> =
        remoteDataSource.getForecast(lat, lon, units, lang)

    override fun getAllFavorites(): Flow<List<FavoriteLocation>> =
        localDataSource.getFavorites()

    override suspend fun addFavorite(location: FavoriteLocation) =
        localDataSource.insertFavorite(location)

    override suspend fun removeFavorite(location: FavoriteLocation) =
        localDataSource.deleteFavorite(location)

    override suspend fun getFavoriteById(id: Int): FavoriteLocation? =
        localDataSource.getFavoriteById(id)

    override fun getUnits(): Flow<String> =
        localDataSource.getUnits()

    override suspend fun setUnits(units: String) =
        localDataSource.setUnits(units)

    override fun getWindUnit(): Flow<String> =
        localDataSource.getWindUnit()

    override suspend fun setWindUnit(unit: String) =
        localDataSource.setWindUnit(unit)

    override fun getLanguage(): Flow<String> =
        localDataSource.getLanguage()

    override suspend fun setLanguage(lang: String) =
        localDataSource.setLanguage(lang)

    override fun getLocationMode(): Flow<String> =
        localDataSource.getLocationMode()

    override suspend fun setLocationMode(mode: String) =
        localDataSource.setLocationMode(mode)

    override fun getManualLocation(): Flow<Pair<Double, Double>?> =
        localDataSource.getManualLocation()

    override suspend fun setManualLocation(lat: Double, lon: Double) =
        localDataSource.setManualLocation(lat, lon)

    override fun getThemeMode(): Flow<String> =
        localDataSource.getThemeMode()

    override suspend fun setThemeMode(mode: String) =
        localDataSource.setThemeMode(mode)
}