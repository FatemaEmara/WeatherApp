package com.example.weatherapp.data.weather.model


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alerts")
data class Alert(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val startTime: Long,
    val endTime: Long,
    val type: String,
    val weatherType: String,
    val isActive: Boolean = true
)