package com.example.weather_app_xml.WeatherAppViewModel

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.*
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

//check how this class works
@ExperimentalCoroutinesApi
class LocationRepository @Inject constructor(
    private val locationClient: FusedLocationProviderClient,
    private val application: Application
) : ILocationRepository {

    @SuppressLint("MissingPermission") //to suppress an error regarding permission from a user
    override suspend fun getLastLocation(): Location? {
        val locationManager =
            application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!application.applicationContext.hasLocationPermission() || !isGpsEnabled) {
            return null
        }

        //initial request
//        locationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
//            override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token
//
//            override fun isCancellationRequested() = false
//        })
//            .addOnSuccessListener { location: Location? ->
//                if (location == null)
//                else {
//                    val lat = location.latitude
//                    val lon = location.longitude
//                }
//
//            }

        return suspendCancellableCoroutine { cont ->
            locationClient.lastLocation.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    cont.resume(task.result, null)
                    Log.d("aaa", "HAS LOCATIONS2")
                } else {
                    cont.resume(null, null)
                }
            }.addOnCanceledListener {
                cont.cancel()
            }.addOnFailureListener {
                cont.cancel()
            }

        }
    }
}