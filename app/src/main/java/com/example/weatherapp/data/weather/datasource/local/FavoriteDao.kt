package com.example.weatherapp.data.weather.datasource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.data.weather.model.FavoriteLocation
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorite_locations")
    fun getAllFavorites(): Flow<List<FavoriteLocation>>

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertFavorite(location: FavoriteLocation)

    @Delete
    suspend fun deleteFavorite(location: FavoriteLocation)

    @Query("SELECT * FROM favorite_locations WHERE id = :id")
    suspend fun getFavoriteById(id: Int): FavoriteLocation?
}