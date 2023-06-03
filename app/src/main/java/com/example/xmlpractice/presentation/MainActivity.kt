package com.example.xmlpractice.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xmlpractice.data.helpers.TimeFormatter
import com.example.xmlpractice.data.remote.CurrentWeatherDataDto
import com.example.xmlpractice.data.remote.DailyWeatherDataDto
import com.example.xmlpractice.middle.adapters.HourlyWeatherRecyclerViewAdapter
import com.example.xmlpractice.data.remote.WeatherDto
import com.example.xmlpractice.databinding.ActivityMainBinding
import com.example.xmlpractice.middle.AppModule
import com.example.xmlpractice.middle.WeatherType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HourlyWeatherRecyclerViewAdapter

    private var API = AppModule.getWeatherApi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        var weatherDto: WeatherDto
        GlobalScope.launch(Dispatchers.IO) {
            val response =
                API.getWeather() // this should be moved somewhere, and mapped to some other types that ui gonna use
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {// run on a main thread to update ui
                    weatherDto = response.body()!!

                    fillCurrentWeather(weatherDto.currentWeather)
                    generateHourlyForecastCards(binding.hourlyForecast, weatherDto)
                    fillTodaysData(weatherDto.dailyWeatherData)
                    binding.timeZone.text = weatherDto.timeZone

                }
            }
        }
    }


    //there should be a ViewModel class that can call AppModule getWeatherApi and then pack all the data in some ui data classes
    //then this activity just sets the data to the ui
    //also some observer pattern has to be implemented
    //but how do I know APi changed its data?
    private fun generateHourlyForecastCards(recyclerView: RecyclerView, weatherDto: WeatherDto) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false);
            adapter = HourlyWeatherRecyclerViewAdapter(weatherDto)
        }
        recyclerView.visibility = View.VISIBLE
    }

    private fun fillCurrentWeather(currentWeather: CurrentWeatherDataDto) {
        binding.currentTemperature.text =
            currentWeather.temperature.toString() // the viewmodel should convert it to string automatically and add a degree sign
        //binding.currentWindSpeed.text = currentWeather.windSpeed.toString()
        val weatherType = WeatherType.fromWMO(currentWeather.weatherCode)
        binding.currentWeatherBigIcon.setImageResource(weatherType.iconRes)
        binding.weatherDescription.text = weatherType.weatherDesc
        binding.wholeLayout.setBackgroundColor(weatherType.backgroundColor)
        //in case of dark colors text color should be white also
        //i believe all this logs should be in a view model and should be provided as data classes to this class
    }

    private fun fillTodaysData(daily : DailyWeatherDataDto) {
        binding.maxTemperature.text = daily.todaysMaxTemperature[0].toString()
        binding.minTemperature.text = daily.todaysMinTemperature[0].toString()
    }

}