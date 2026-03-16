package com.example.weatherapp.service.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.weatherapp.data.network.WeatherNetwork
import com.example.weatherapp.data.weather.AppRepositoryImpl
import com.example.weatherapp.data.weather.datasource.local.AppDatabase
import com.example.weatherapp.data.weather.datasource.local.LocalDataSourceImpl
import com.example.weatherapp.data.weather.datasource.remote.WeatherRemoteDataSourceImpl
import com.example.weatherapp.utils.ResponseState
import kotlinx.coroutines.flow.first

class NotificationWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        return try {
            val db = AppDatabase.getInstance(context)
            val repository = AppRepositoryImpl(
                remoteDataSource = WeatherRemoteDataSourceImpl(WeatherNetwork.weatherService),
                localDataSource = LocalDataSourceImpl(
                    context = context,
                    favoriteDao = db.favoriteDao(),
                    alertDao = db.alertDao()
                )
            )

            val locationMode = repository.getLocationMode().first()

            val location = when (locationMode) {
                "map" -> repository.getManualLocation().first()
                else -> repository.getLastKnownLocation().first()
            }

            if (location == null) {
                NotificationHelper.showErrorNotification(
                    context, "Open the app once to load your location."
                )
                return Result.success()
            }

            val units = repository.getUnits().first()
            val lang = repository.getLanguage().first()

            var notified = false
            repository.getCurrentWeather(
                lat = location.first,
                lon = location.second,
                units = units,
                lang = lang
            ).collect { result ->

                when (result) {
                    is ResponseState.Success -> {
                        if (!notified) {
                            notified = true
                            val weather = result.data
                            val description = weather.weather.firstOrNull()
                                ?.description
                                ?.replaceFirstChar { it.uppercase() } ?: "—"
                            val unitLabel = when (units) {
                                "imperial" -> "°F"
                                "standard" -> "K"
                                else -> "°C"
                            }

                            NotificationHelper.showWeatherNotification(
                                context = context,
                                cityName = weather.name,
                                description = description,
                                temp = weather.main.temp,
                                humidity = weather.main.humidity,
                                windSpeed = weather.wind.speed,
                                unitLabel = unitLabel
                            )
                        }
                    }

                    is ResponseState.Failure -> {
                        if (!notified) {
                            notified = true
                            NotificationHelper.showErrorNotification(
                                context, "Could not fetch weather. Check your connection."
                            )
                        }
                    }

                    is ResponseState.Loading -> {

                    }

                    else -> {
                    }
                }
            }

            Result.success()

        } catch (e: Exception) {
            Result.retry()
        }
    }

}