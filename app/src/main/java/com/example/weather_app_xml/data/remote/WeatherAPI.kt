package com.example.weather_app_xml.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

public interface WeatherAPI {
    @GET("v1/forecast?" +
            "&forecast_days=11" + // to show 10 days forecast without current day
            "&hourly=temperature_2m,weathercode" +
            "&current_weather=1" +
            "&daily=weathercode,temperature_2m_max,temperature_2m_min" +
            "&timezone=auto")
    suspend fun getWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") long: Double
    ): Response<WeatherDto>
}