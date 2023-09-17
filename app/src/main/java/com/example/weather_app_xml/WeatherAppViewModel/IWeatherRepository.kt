package com.example.weather_app_xml.WeatherAppViewModel

import androidx.lifecycle.MutableLiveData

enum class State {
    LOADING,
    ERROR,
    SUCCESS
}

data class Weather(
    var weather: WeatherDataHolder? = null,
    var current_state: State? = State.LOADING
)
//this is singleton works with binds of hilt
interface IWeatherRepository {
    suspend fun getWeatherData(lat : Double, long : Double): Weather
    fun getLiveData() : MutableLiveData<State?>
    fun getDefaultState() : State
}