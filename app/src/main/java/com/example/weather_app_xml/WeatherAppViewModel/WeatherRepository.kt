package com.example.weather_app_xml.WeatherAppViewModel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.weather_app_xml.data.WeatherTypesMapper
import com.example.weather_app_xml.data.remote.WeatherAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ExperimentalCoroutinesApi
class WeatherRepository @Inject constructor(
    private val api: WeatherAPI,
    private val applicaton: Application
) : IWeatherRepository {

    private val m_weather = Weather() // null
    private val m_mapper = WeatherTypesMapper()

    private val liveData = MutableLiveData<State?>().apply {
        value = getDefaultState()
    }
    override suspend fun getWeatherData(lat: Double, long: Double): Weather {
        if (!hasInternetConnection(applicaton)) {
            m_weather.current_state = State.ERROR
            liveData.value = m_weather.current_state
            Log.d("AAA", "NO INTERNET")
            return Weather()
        }

        m_weather.current_state = State.LOADING
        liveData.value = m_weather.current_state
        Log.d("AAA", "INTERNET")
        CoroutineScope(Dispatchers.IO).launch {
            val response = api.getWeather(lat, long)
            if (response.isSuccessful) {
                m_weather.weather =
                    m_mapper.mapDTOsToWeatherData(response.body()!!) // we are sure that it's not null
                m_weather.current_state = State.SUCCESS
            } else {
                m_weather.current_state = State.ERROR
            }

            withContext(Dispatchers.Main) {
                liveData.value = m_weather.current_state
            }

        }
        Log.d("WeatherService", m_weather.current_state.toString())
        return m_weather
    }

    override fun getLiveData(): MutableLiveData<State?> {
        return liveData
    }

    override fun getDefaultState(): State {
        return State.ERROR
    }

    private fun hasInternetConnection(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        if (capabilities != null) {
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        }

        return false
    }

}

