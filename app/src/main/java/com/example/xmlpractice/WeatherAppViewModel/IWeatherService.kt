package com.example.xmlpractice.WeatherAppViewModel

enum class State {
    LOADING,
    ERROR,
    SUCCESS
}

data class Weather(
    var weather: WeatherDataHolder? = null,
    var current_state: State? = State.ERROR
)

interface IWeatherService {
    fun getWeatherData(lat : Double, long : Double): Weather
    fun setWeatherStateListener(listener: WeatherStateListener)
}