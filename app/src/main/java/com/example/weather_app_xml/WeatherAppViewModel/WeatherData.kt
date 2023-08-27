package com.example.weather_app_xml.WeatherAppViewModel

data class WeatherDataHolder(
    val data_hourly: WeatherDataHourly?,
    val data_today: WeatherDataToday?,
    val data_current: WeatherDataCurrent?,
    val data_timezone: WeatherDataTimezone?
)

data class WeatherDataHourly(
    val hours : List<String>,
    val temperatures : List<String>,
    val weather_codes : List<Int>
)

data class WeatherDataToday(
    val max_temperature : String,
    val min_temperature : String
)

data class WeatherDataCurrent(
    val temperature : String,
    val weather_code : Int,
    val wind_speed : String
)

data class WeatherDataTimezone(
    val timezone : String
)


