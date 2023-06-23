package com.example.xmlpractice.WeatherAppViewModel

data class WeatherDataHolder(
    val hourly: WeatherDataHourly?,
    val today: WeatherDataToday?,
    val current: WeatherDataCurrent?,
    val timezone: WeatherDataTimezone?
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
    val wind_speed : Double
)

data class WeatherDataTimezone(
    val timezone : String
)


