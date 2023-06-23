package com.example.xmlpractice.WeatherAppViewModel

import android.util.Log
import com.example.xmlpractice.data.WeatherTypesMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WeatherService : IWeatherService {

    private var weather = Weather()
    private val mapper = WeatherTypesMapper()
    private var API = AppModule.getWeatherApi()
    private var weatherStateListener: WeatherStateListener? = null

    override fun getWeatherData(): Weather {
        weather = weather.copy(
            current_state = State.LOADING
        )
        notifyWeatherStateChanged(weather.current_state!!)

        Log.d("SERVICE", "LOADING")
        GlobalScope.launch(Dispatchers.IO) {
            Log.d("SERVICE", "LOADING 2222")
            val response = API.getWeather()
            if (response.isSuccessful) {
                weather.weather = mapper.mapDTOsToWeatherData(response.body()!!)
                weather.current_state = State.SUCCESS

                notifyWeatherStateChanged(weather.current_state!!)

                Log.d("SERVICE", "SUCCESSSSS")
            } else {
                weather = weather.copy(
                    current_state = State.ERROR
                )
                notifyWeatherStateChanged(weather.current_state!!)//&&&&&&&&&&&&&&?????????????
                Log.d("SERVICE", "ERROR")
            }

        }
        return weather
    }

    override fun setWeatherStateListener(listener: WeatherStateListener) {
        weatherStateListener = listener
    }

    private fun notifyWeatherStateChanged(state: State) {
        weatherStateListener?.onWeatherStateChanged(state)
    }

}

