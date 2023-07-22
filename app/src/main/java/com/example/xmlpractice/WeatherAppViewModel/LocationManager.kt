package com.example.xmlpractice.WeatherAppViewModel

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import android.Manifest

class LocationManager(
    private val context: Context
) {
    private lateinit var m_locationListener : LocationListener
    private var m_fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            result.let {
                for (location in result.locations) {
                    m_locationListener.onLocationUpdate(location.latitude, location.longitude)
                }
            }
        }
    }

    fun setLocationListener(locationListener: LocationListener) {
        m_locationListener = locationListener
    }

    fun startLocationUpdates() {
        if (context.hasLocationPermission()) { // just in case
            requestLocationUpdates()
        } else {
            m_locationListener.onPermissionDenied()
        }
    }

    @SuppressLint("MissingPermission") //to suppress an error regarding permission from a user -> it's checked in startLocationUpdates
    private fun requestLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 1000
            smallestDisplacement = 1000f // location update will be triggered only if location changed
        }
        m_fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun stopLocationUpdates() {
        m_fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}