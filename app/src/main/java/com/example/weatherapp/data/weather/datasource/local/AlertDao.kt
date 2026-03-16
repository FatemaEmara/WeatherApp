package com.example.weatherapp.data.weather.datasource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.weatherapp.data.weather.model.Alert
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {

    @Query("SELECT * FROM alerts ORDER BY startTime ASC")
    fun getAllAlerts(): Flow<List<Alert>>

    @Query("SELECT * FROM alerts WHERE id = :id")
    suspend fun getAlertById(id: Int): Alert?

    @Query("SELECT * FROM alerts WHERE isActive = 1")
    suspend fun getActiveAlerts(): List<Alert>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: Alert): Long

    @Update
    suspend fun updateAlert(alert: Alert)

    @Delete
    suspend fun deleteAlert(alert: Alert)

    @Query("DELETE FROM alerts")
    suspend fun deleteAllAlerts()

    @Query("UPDATE alerts SET isActive = 0 WHERE id = :id")
    suspend fun deactivateAlert(id: Int)
}