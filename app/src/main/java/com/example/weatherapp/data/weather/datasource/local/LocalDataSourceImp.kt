package com.example.weatherapp.data.weather.datasource.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.weatherapp.data.weather.model.Alert
import com.example.weatherapp.data.weather.model.FavoriteLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences>
        by preferencesDataStore(name = "weather_settings")

class LocalDataSourceImpl(
    private val context: Context,
    private val favoriteDao: FavoriteDao,
    private val alertDao: AlertDao
) : LocalDataSource {

    private val UNITS_KEY = stringPreferencesKey("units")
    private val WIND_UNIT_KEY = stringPreferencesKey("wind_unit")
    private val LANG_KEY = stringPreferencesKey("lang")
    private val LOCATION_MODE_KEY = stringPreferencesKey("location_mode")
    private val MANUAL_LAT_KEY = stringPreferencesKey("manual_lat")
    private val MANUAL_LON_KEY = stringPreferencesKey("manual_lon")
    private val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
    private val LAST_LAT_KEY           = stringPreferencesKey("last_lat")
    private val LAST_LON_KEY           = stringPreferencesKey("last_lon")

    override fun getFavorites(): Flow<List<FavoriteLocation>> =
        favoriteDao.getAllFavorites()


    override suspend fun insertFavorite(location: FavoriteLocation) =
        favoriteDao.insertFavorite(location)


    override suspend fun deleteFavorite(location: FavoriteLocation) =
        favoriteDao.deleteFavorite(location)


    override suspend fun getFavoriteById(
        id: Int
    ): FavoriteLocation? = favoriteDao.getFavoriteById(id)

    override fun getAllAlerts(): Flow<List<Alert>> =alertDao.getAllAlerts()

    override suspend fun getAlertById(id: Int): Alert? = alertDao.getAlertById(id)

    override suspend fun getActiveAlerts(): List<Alert> =alertDao.getActiveAlerts()

    override suspend fun insertAlert(alert: Alert): Long =alertDao.insertAlert(alert)

    override suspend fun updateAlert(alert: Alert) =alertDao.updateAlert(alert)

    override suspend fun deleteAlert(alert: Alert) = alertDao.deleteAlert(alert)

    override suspend fun deactivateAlert(id: Int) =alertDao.deactivateAlert(id)

    override suspend fun deleteAllAlerts() = alertDao.deleteAllAlerts()


    override fun getUnits(): Flow<String> =
        context.dataStore.data.map { it[UNITS_KEY] ?: "metric" }

    override suspend fun setUnits(units: String) {
        context.dataStore.edit { it[UNITS_KEY] = units }
    }

    override fun getWindUnit(): Flow<String> =
        context.dataStore.data.map { it[WIND_UNIT_KEY] ?: "ms" }

    override suspend fun setWindUnit(unit: String) {
        context.dataStore.edit { it[WIND_UNIT_KEY] = unit }
    }

    override fun getLanguage(): Flow<String> =
        context.dataStore.data.map { it[LANG_KEY] ?: "en" }

    override suspend fun setLanguage(lang: String) {
        context.dataStore.edit { it[LANG_KEY] = lang }
    }

    override fun getLocationMode(): Flow<String> =
        context.dataStore.data.map { it[LOCATION_MODE_KEY] ?: "gps" }

    override suspend fun setLocationMode(mode: String) {
        context.dataStore.edit { it[LOCATION_MODE_KEY] = mode }
    }

    override fun getManualLocation(): Flow<Pair<Double, Double>?> =
        context.dataStore.data.map { prefs ->
            val lat = prefs[MANUAL_LAT_KEY]?.toDoubleOrNull()
            val lon = prefs[MANUAL_LON_KEY]?.toDoubleOrNull()
            if (lat != null && lon != null) lat to lon else null
        }

    override suspend fun setManualLocation(lat: Double, lon: Double) {
        context.dataStore.edit {
            it[MANUAL_LAT_KEY] = lat.toString()
            it[MANUAL_LON_KEY] = lon.toString()
        }
    }

    override fun getThemeMode(): Flow<String> =
        context.dataStore.data.map { it[THEME_MODE_KEY] ?: "system" }

    override suspend fun setThemeMode(mode: String) {
        context.dataStore.edit { it[THEME_MODE_KEY] = mode }
    }

    override fun getLastKnownLocation(): Flow<Pair<Double, Double>?> =
        context.dataStore.data.map { prefs ->
            val lat = prefs[LAST_LAT_KEY]?.toDoubleOrNull()
            val lon = prefs[LAST_LON_KEY]?.toDoubleOrNull()
            if (lat != null && lon != null) lat to lon else null
        }

    override suspend fun setLastKnownLocation(lat: Double, lon: Double) {
        context.dataStore.edit {
            it[LAST_LAT_KEY] = lat.toString()
            it[LAST_LON_KEY] = lon.toString()
        }
    }
}