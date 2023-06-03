package com.example.xmlpractice.data.remote

import retrofit2.Response
import retrofit2.http.GET

public interface WeatherAPI {
    @GET("v1/forecast?latitude=52.52&longitude=13.41" +
            "&forecast_days=1" +
            "&hourly=temperature_2m,weathercode" +
            "&current_weather=1" +
            "&daily=temperature_2m_max,temperature_2m_min" +
            "&timezone=auto")
    suspend fun getWeather(
    ): Response<WeatherDto>
}