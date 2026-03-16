package com.example.weatherapp.service.alert

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.weatherapp.data.network.WeatherNetwork
import com.example.weatherapp.data.weather.AppRepositoryImpl
import com.example.weatherapp.data.weather.datasource.local.AppDatabase
import com.example.weatherapp.data.weather.datasource.local.LocalDataSourceImpl
import com.example.weatherapp.data.weather.datasource.remote.WeatherRemoteDataSourceImpl
import com.example.weatherapp.utils.ResponseState
import kotlinx.coroutines.flow.first

class AlertWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    companion object {
        private const val TAG = "AlertWorker"

        const val WEATHER_RAIN = "rain"
        const val WEATHER_SNOW = "snow"
        const val WEATHER_STORM = "storm"
        const val WEATHER_FOG = "fog"
        const val WEATHER_HEAT = "heat"
        const val WEATHER_COLD = "cold"
        const val WEATHER_WIND = "wind"
        const val WEATHER_ANY = "any"
    }

    override suspend fun doWork(): Result {
        val alertId = inputData.getInt(AlertScheduler.EXTRA_ALERT_ID, -1)
        val alertType = inputData.getString(AlertScheduler.EXTRA_ALERT_TYPE) ?: "notification"
        val weatherType = inputData.getString(AlertScheduler.EXTRA_WEATHER_TYPE) ?: WEATHER_ANY


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
            Log.d(TAG, "locationMode=$locationMode")

            val location = when (locationMode) {
                "map" -> repository.getManualLocation().first()
                else -> repository.getLastKnownLocation().first()
            }
            Log.d(TAG, "location=$location")

            if (location == null) {
                Log.w(TAG, "No location available")
                return Result.success()
            }

            val units = repository.getUnits().first()
            val lang = repository.getLanguage().first()
            Log.d(TAG, "fetching weather: lat=${location.first} lon=${location.second}")

            var processed = false

            repository.getCurrentWeather(
                lat = location.first,
                lon = location.second,
                units = units,
                lang = lang
            ).collect { result ->
                Log.d(TAG, "result type = ${result::class.simpleName}")

                when (result) {
                    is ResponseState.Success -> {
                        if (!processed) {
                            processed = true

                            val weather = result.data
                            val description =
                                weather.weather.firstOrNull()?.description?.lowercase() ?: ""
                            val weatherId = weather.weather.firstOrNull()?.id ?: 0
                            val temp = weather.main.temp
                            val windSpeed = weather.wind.speed
                            val unitLabel = when (units) {
                                "imperial" -> "°F"
                                "standard" -> "K"
                                else -> "°C"
                            }

                            Log.d(
                                TAG,
                                "weather: id=$weatherId desc=$description temp=$temp wind=$windSpeed"
                            )

                            val conditionMet = when (weatherType) {
                                WEATHER_RAIN -> weatherId in 300..531
                                WEATHER_SNOW -> weatherId in 600..622
                                WEATHER_STORM -> weatherId in 200..232
                                WEATHER_FOG -> weatherId in 701..781
                                WEATHER_HEAT -> temp > 40.0
                                WEATHER_COLD -> temp < 0.0
                                WEATHER_WIND -> windSpeed > 10.0
                                WEATHER_ANY -> true
                                else -> false
                            }


                            if (conditionMet) {
                                val title = " Weather Alert"
                                val message =
                                    "${description.replaceFirstChar { it.uppercase() }} • " +
                                            "${temp.toInt()}$unitLabel in ${weather.name}"


                                val notification = AlertReceiver.buildAlarmNotification(
                                    context = context,
                                    alertId = alertId,
                                    title = title,
                                    message = message
                                )

                                context.getSystemService(NotificationManager::class.java)
                                    .notify(alertId, notification)

                                if (alertType == "alarm" && !AlarmSoundHelper.isPlaying()) {
                                    Log.d(TAG, "Starting alarm sound")
                                    AlarmSoundHelper.startAlarm(context)
                                }
                            } else {
                                Log.d(TAG, "Condition NOT met — no alarm")
                            }
                        }
                    }

                    is ResponseState.Failure -> {
                    }

                    is ResponseState.Loading -> {
                        Log.d(TAG, "Loading...")
                    }

                    else -> {
                        Log.w(TAG, "Unknown state: ${result::class.simpleName}")
                    }
                }
            }

            Result.success()

        } catch (e: Exception) {
            Result.retry()
        }
    }
}

