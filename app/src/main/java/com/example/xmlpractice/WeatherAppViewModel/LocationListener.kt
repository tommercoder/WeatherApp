package com.example.xmlpractice.WeatherAppViewModel

interface LocationListener {
    fun onLocationUpdate(latitude: Double, longitude: Double)
    fun onPermissionDenied()
}