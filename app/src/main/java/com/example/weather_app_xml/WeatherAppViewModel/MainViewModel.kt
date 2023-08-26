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
    private val weatherRepository: IWeatherRepository,
    private val locationRepository: ILocationRepository
) : ViewModel() {

    var weatherData : Weather? = null
    val liveData: MutableLiveData<State?> = weatherRepository.getLiveData()
    fun loadWeather() {
        viewModelScope.launch {
            locationRepository.getLastLocation()?.let { location ->
                weatherData = weatherRepository.getWeatherData(location.latitude, location.longitude)
            }
        }
    }

}