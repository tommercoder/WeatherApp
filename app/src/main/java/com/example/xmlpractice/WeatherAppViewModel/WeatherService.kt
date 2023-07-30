package com.example.xmlpractice.WeatherAppViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.xmlpractice.data.WeatherTypesMapper
import com.example.xmlpractice.data.remote.WeatherAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
@ExperimentalCoroutinesApi
class WeatherService @Inject constructor(
    private val api: WeatherAPI
) : IWeatherService {

    private val m_weather = Weather() // null
    private val m_mapper = WeatherTypesMapper()
    //private val m_API = AppModule.getWeatherApi()
    private lateinit var m_weatherStateListener: WeatherStateListener
    private val liveData = MutableLiveData<State?>()
    //todo: make possible to notify the ViewModel about state changes
    override suspend fun getWeatherData(lat : Double, long : Double): Weather {
        m_weather.current_state = State.LOADING
        //notifyWeatherStateChanged(m_weather.current_state!!)
        //liveData.value = m_weather.current_state
        GlobalScope.launch(Dispatchers.IO) {
            val response = api.getWeather(lat, long)
            if (response.isSuccessful) {
                m_weather.weather = m_mapper.mapDTOsToWeatherData(response.body()!!) // we are sure that it's not null
                m_weather.current_state = State.SUCCESS
                //liveData.value = m_weather.current_state
                //notifyWeatherStateChanged(m_weather.current_state!!)
            }
            else {
                m_weather.current_state = State.ERROR
                //liveData.value = m_weather.current_state
                //notifyWeatherStateChanged(m_weather.current_state!!)
            }

        }
        return m_weather
    }

    override fun getLiveData(): MutableLiveData<State?> {
        return liveData
    }
//    override fun setWeatherStateListener(listener: WeatherStateListener) {
//        m_weatherStateListener = listener
//    }
//
//    private fun notifyWeatherStateChanged(state: State) {
//        m_weatherStateListener.onWeatherStateChanged(state)
//    }

}

