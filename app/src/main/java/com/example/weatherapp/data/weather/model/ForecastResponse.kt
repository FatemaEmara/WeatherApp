package com.example.weatherapp.data.weather.model

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<ForecastItem>,
    val city: City
)

data class ForecastItem(
    val dt: Long,
    val main: ForecastMain,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val visibility: Int,
    val pop: Double,
    val sys: ForecastSys,
    @SerializedName("dt_txt") val dtTxt: String
)

data class ForecastMain(
    val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("temp_min") val tempMin: Double,
    @SerializedName("temp_max") val tempMax: Double,
    val pressure: Int,
    @SerializedName("sea_level") val seaLevel: Int,
    @SerializedName("grnd_level") val grndLevel: Int,
    val humidity: Int,
    @SerializedName("temp_kf") val tempKf: Double
)

data class ForecastSys(
    val pod: String
)

data class City(
    val id: Int,
    val name: String,
    val coord: Coord,
    val country: String,
    val population: Int,
    val timezone: Int,
    val sunrise: Long,
    val sunset: Long
)