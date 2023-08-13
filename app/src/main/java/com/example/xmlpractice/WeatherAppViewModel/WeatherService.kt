package com.example.xmlpractice.WeatherAppViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.xmlpractice.data.WeatherTypesMapper
import com.example.xmlpractice.data.remote.WeatherAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ExperimentalCoroutinesApi
class WeatherService @Inject constructor(
    private val api: WeatherAPI
) : IWeatherService {

    private val m_weather = Weather() // null
    private val m_mapper = WeatherTypesMapper()

    private val liveData = MutableLiveData<State?>()
    override suspend fun getWeatherData(lat: Double, long: Double): Weather {
        m_weather.current_state = State.LOADING
        liveData.value = m_weather.current_state

        CoroutineScope(Dispatchers.IO).launch {
            val response = api.getWeather(lat, long)
            if (response.isSuccessful) {
                m_weather.weather =
                    m_mapper.mapDTOsToWeatherData(response.body()!!) // we are sure that it's not null
                m_weather.current_state = State.SUCCESS

                //update
                withContext(Dispatchers.Main) {
                    liveData.value = m_weather.current_state
                }
            } else {
                m_weather.current_state = State.ERROR
                //update
                withContext(Dispatchers.Main) {
                    liveData.value = m_weather.current_state
                }
            }
        }
        return m_weather
    }

    override fun getLiveData(): MutableLiveData<State?> {
        return liveData
    }

}

