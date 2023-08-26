package com.example.weather_app_xml.WeatherAppViewModel

import android.location.Location

//this is singleton works with binds of hilt
interface ILocationRepository {
    suspend fun getLastLocation() : Location?
}