package com.example.xmlpractice.WeatherAppViewModel

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

@ExperimentalCoroutinesApi
class LocationService @Inject constructor(
    private val locationClient: FusedLocationProviderClient
) : ILocationService {

    @SuppressLint("MissingPermission") //to suppress an error regarding permission from a user -> it's checked in startLocationUpdates
    override suspend fun getLastLocation(): Location? {
        //todo: //check permission just in case

        return suspendCancellableCoroutine { cont ->
            locationClient.lastLocation.apply {
                if (isComplete) {
                    if (isSuccessful) {
                        cont.resume(result, null)
                    } else {
                        cont.resume(null, null)
                    }
                    return@suspendCancellableCoroutine
                }
                addOnSuccessListener {
                    cont.resume(it, null)
                }
                addOnFailureListener {
                    cont.resume(null, null)
                }
                addOnCanceledListener {
                    cont.cancel()
                }
            }
        }
    }
}