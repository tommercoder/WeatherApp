package com.example.xmlpractice.WeatherAppViewModel

import android.util.Log
import com.example.xmlpractice.data.WeatherTypesMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WeatherService() : IWeatherService {

    private val m_weather = Weather() // null
    private val m_mapper = WeatherTypesMapper()
    private val m_API = AppModule.getWeatherApi()
    private lateinit var m_weatherStateListener: WeatherStateListener

    override fun getWeatherData(lat : Double, long : Double): Weather { // do I need another approach with lat/long?
        m_weather.current_state = State.LOADING
        notifyWeatherStateChanged(m_weather.current_state!!)

        GlobalScope.launch(Dispatchers.IO) {
            val response = m_API.getWeather(lat, long)
            if (response.isSuccessful) {
                m_weather.weather = m_mapper.mapDTOsToWeatherData(response.body()!!) // we are sure that it's not null
                m_weather.current_state = State.SUCCESS

                notifyWeatherStateChanged(m_weather.current_state!!)
            }
            else {
                m_weather.current_state = State.ERROR
                notifyWeatherStateChanged(m_weather.current_state!!)
            }

        }
        return m_weather
    }

    override fun setWeatherStateListener(listener: WeatherStateListener) {
        m_weatherStateListener = listener
    }

    private fun notifyWeatherStateChanged(state: State) {
        m_weatherStateListener.onWeatherStateChanged(state)
    }

}

