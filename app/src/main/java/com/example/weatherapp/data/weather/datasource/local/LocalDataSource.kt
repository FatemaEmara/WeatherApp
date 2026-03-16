package com.example.weatherapp.data.weather.datasource.local

import com.example.weatherapp.data.weather.model.Alert
import com.example.weatherapp.data.weather.model.FavoriteLocation
import kotlinx.coroutines.flow.Flow


interface LocalDataSource {

    fun getFavorites(): Flow<List<FavoriteLocation>>
    suspend fun insertFavorite(favorite: FavoriteLocation)
    suspend fun deleteFavorite(favorite: FavoriteLocation)
    suspend fun getFavoriteById(id: Int): FavoriteLocation?

    fun getAllAlerts(): Flow<List<Alert>>
    suspend fun getAlertById(id: Int): Alert?
    suspend fun getActiveAlerts(): List<Alert>
    suspend fun insertAlert(alert: Alert): Long
    suspend fun updateAlert(alert: Alert)
    suspend fun deleteAlert(alert: Alert)
    suspend fun deactivateAlert(id: Int)
    suspend fun deleteAllAlerts()

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


    fun getLastKnownLocation(): Flow<Pair<Double, Double>?>
    suspend fun setLastKnownLocation(lat: Double, lon: Double)

}
