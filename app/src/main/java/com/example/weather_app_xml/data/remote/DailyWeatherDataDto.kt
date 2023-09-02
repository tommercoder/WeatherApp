package com.example.weather_app_xml.data.remote

import com.google.gson.annotations.SerializedName

data class DailyWeatherDataDto(
    @SerializedName("temperature_2m_max")
    val maxTemperatures: List<Double>,
    @SerializedName("temperature_2m_min")
    val minTemperatures: List<Double>,
    @SerializedName("weathercode")
    val weatherCodes: List<Int>,
    @SerializedName("time")
    val dates: List<String>
)
