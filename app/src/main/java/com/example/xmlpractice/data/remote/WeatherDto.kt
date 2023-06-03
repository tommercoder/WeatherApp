package com.example.xmlpractice.data.remote

import com.google.gson.annotations.SerializedName

data class WeatherDto(
    @SerializedName("daily")
    val dailyWeatherData : DailyWeatherDataDto,
    @SerializedName("hourly")
    val hourlyWeatherData: HourlyWeatherDataDto,
    @SerializedName("current_weather")
    val currentWeather: CurrentWeatherDataDto,
    @SerializedName("timezone")
    val timeZone : String
)
