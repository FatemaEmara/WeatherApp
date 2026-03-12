package com.example.weatherapp.data.weather.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapp.data.weather.datasource.local.FavoriteDao
import com.example.weatherapp.data.weather.model.FavoriteLocation

@Database(entities = [FavoriteLocation::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "weather_app_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}