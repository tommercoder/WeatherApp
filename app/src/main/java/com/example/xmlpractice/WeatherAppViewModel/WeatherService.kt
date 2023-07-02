package com.example.xmlpractice.WeatherAppViewModel

import android.util.Log
import com.example.xmlpractice.data.WeatherTypesMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WeatherService : IWeatherService {

    private var weather = Weather() // null
    private lateinit var weatherStateListener: WeatherStateListener
    private val mapper = WeatherTypesMapper()
    private var API = AppModule.getWeatherApi()

    override fun getWeatherData(): Weather {
        weather.current_state = State.LOADING
        notifyWeatherStateChanged(weather.current_state!!)
        Log.d("SERVICE", "LOADING!")

        GlobalScope.launch(Dispatchers.IO) {
            val response = API.getWeather()
            if (response.isSuccessful) {
                weather.weather = mapper.mapDTOsToWeatherData(response.body()!!) // we are sure that it's not null
                weather.current_state = State.SUCCESS

                notifyWeatherStateChanged(weather.current_state!!)

                Log.d("SERVICE", "SUCCESS!")
            } else {
                weather.current_state = State.ERROR
                notifyWeatherStateChanged(weather.current_state!!)
                Log.d("SERVICE", "ERROR!")
            }

        }
        return weather
    }

    override fun setWeatherStateListener(listener: WeatherStateListener) {
        weatherStateListener = listener
    }

    private fun notifyWeatherStateChanged(state: State) {
        weatherStateListener.onWeatherStateChanged(state)
    }

}

