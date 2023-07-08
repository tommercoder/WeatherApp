package com.example.xmlpractice.WeatherAppViewModel

interface ILocationService {
    fun requestUsersLocationPermission()
    fun isLocationPermissionGranted() : Boolean
}