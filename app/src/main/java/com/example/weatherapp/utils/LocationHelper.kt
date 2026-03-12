package com.example.weatherapp.utils


import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class LocationHelper(private val context: Context) {

    private val fusedClient =
        LocationServices.getFusedLocationProviderClient(context)

    fun isLocationEnabled(): Boolean {
        val manager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    fun getFreshLocation(onLocation: (Location?) -> Unit) {
        fusedClient.requestLocationUpdates(
            LocationRequest.Builder(30_000L)
                .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
                .setMinUpdateDistanceMeters(500f)
                .build(),
            object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    onLocation(result.lastLocation)
                }
            },
            Looper.getMainLooper()
        )
    }


}