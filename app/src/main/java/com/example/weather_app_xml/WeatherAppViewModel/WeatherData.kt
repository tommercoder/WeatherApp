package com.example.weather_app_xml.WeatherAppViewModel

data class WeatherDataHolder(
    val data_hourly: WeatherDataHourly?,
    val data_today: WeatherDataToday?,
    val data_current: WeatherDataCurrent?,
    val data_timezone: WeatherDataTimezone?,
    val data_daily: WeatherDataDaily?
)

data class WeatherDataHourly(
    val hours: List<String>,
    val temperatures: List<String>,
    val weather_codes: List<Int>
)

data class WeatherDataToday(
    val max_temperature: String,
    val min_temperature: String
)

data class WeatherDataDaily(
    val dates: List<String>,
    val weather_codes: List<Int>,
    val lowest_temps: List<String>,
    val highest_temps: List<String>
)

data class WeatherDataCurrent(
    val current_hour: Int,
    val temperature: String,
    val weather_code: Int,
    val wind_speed: String
)

data class WeatherDataTimezone(
    val timezone: String
)

data class DetailedDailyWeather(
    val daily_date: String,
    val daily_weather_code: Int,
    val daily_lowest_temp: String,
    val daily_highest_temp: String,
    val hourly: WeatherDataHourly?
)


