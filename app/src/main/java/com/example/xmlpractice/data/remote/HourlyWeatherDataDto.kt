package com.example.xmlpractice.data.remote

import com.google.gson.annotations.SerializedName

data class HourlyWeatherDataDto(
    val time: List<String>,
    @SerializedName("temperature_2m")
    val temperatures: List<Double>,
    @SerializedName("weathercode")
    val weatherCodes: List<Int>
)
