package com.example.xmlpractice.WeatherAppViewModel

import android.location.Location

interface ILocationService {
    suspend fun getLastLocation() : Location?
}