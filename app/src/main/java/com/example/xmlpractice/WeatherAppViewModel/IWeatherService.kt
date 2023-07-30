package com.example.xmlpractice.WeatherAppViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

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
    suspend fun getWeatherData(lat : Double, long : Double): Weather
    fun getLiveData() : MutableLiveData<State?>
    //fun setWeatherStateListener(listener: WeatherStateListener)
}