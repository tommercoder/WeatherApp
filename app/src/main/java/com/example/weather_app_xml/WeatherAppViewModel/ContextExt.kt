package com.example.weather_app_xml.WeatherAppViewModel

import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.Context

//probably not needed
fun Context.hasLocationPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

}