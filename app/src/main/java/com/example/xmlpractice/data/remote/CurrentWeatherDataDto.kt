package com.example.xmlpractice.data.remote

import com.google.gson.annotations.SerializedName

data class CurrentWeatherDataDto(
    val time: String,
    val temperature: Double,
    @SerializedName("weathercode")
    val weatherCode: Int,
    @SerializedName("windspeed")
    val windSpeed: Double
)