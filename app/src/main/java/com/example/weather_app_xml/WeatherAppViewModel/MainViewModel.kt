package com.example.weather_app_xml.WeatherAppViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val weatherService: IWeatherService,
    private val locationService: ILocationService
) : ViewModel() {

    var weatherData : Weather? = null
    val liveData: MutableLiveData<State?> = weatherService.getLiveData()
    fun loadWeather() {
        viewModelScope.launch {
            locationService.getLastLocation()?.let { location ->
                weatherData = weatherService.getWeatherData(location.latitude, location.longitude)
            }
        }
    }

}