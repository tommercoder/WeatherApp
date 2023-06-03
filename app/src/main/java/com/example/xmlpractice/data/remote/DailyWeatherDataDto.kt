package com.example.xmlpractice.data.remote

import com.google.gson.annotations.SerializedName

data class DailyWeatherDataDto(
    @SerializedName("temperature_2m_max")
    val todaysMaxTemperature: List<Double>,
    @SerializedName("temperature_2m_min")
    val todaysMinTemperature: List<Double>
)
