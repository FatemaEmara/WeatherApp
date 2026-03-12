package com.example.weatherapp.data.weather.model


import com.google.gson.annotations.SerializedName

data class LocationResult(
    @SerializedName("place_id") val placeId: Long,
    @SerializedName("display_name") val displayName: String,
    val lat: String,
    val lon: String,
    val address: NominatimAddress?
)

data class NominatimAddress(
    val city: String?,
    val town: String?,
    val village: String?,
    val county: String?,
    val state: String?,
    @SerializedName("country_code") val countryCode: String?
) {
    val resolvedCity: String
        get() = city ?: town ?: village ?: county ?: state ?: "Unknown"
}