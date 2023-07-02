package com.example.xmlpractice.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xmlpractice.presentation.adapters.HourlyWeatherRecyclerViewAdapter
import com.example.xmlpractice.databinding.ActivityMainBinding
import com.example.xmlpractice.WeatherAppViewModel.IWeatherService
import com.example.xmlpractice.WeatherAppViewModel.State
import com.example.xmlpractice.WeatherAppViewModel.Weather
import com.example.xmlpractice.WeatherAppViewModel.WeatherDataCurrent
import com.example.xmlpractice.WeatherAppViewModel.WeatherDataHourly
import com.example.xmlpractice.WeatherAppViewModel.WeatherDataToday
import com.example.xmlpractice.WeatherAppViewModel.WeatherService
import com.example.xmlpractice.WeatherAppViewModel.WeatherStateListener
import com.example.xmlpractice.WeatherAppViewModel.WeatherType

class MainActivity : AppCompatActivity(), WeatherStateListener {

    private lateinit var binding: ActivityMainBinding
    private val service: IWeatherService = WeatherService()
    private lateinit var data: Weather

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        service.setWeatherStateListener(this) // this activity listens to state of the API data
        data = service.getWeatherData() // initial API request
    }

    override fun onWeatherStateChanged(state: State) {
        when (state) {
            State.LOADING -> {
                runOnUiThread {
                    binding.wholeLayout.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }
            }

            State.SUCCESS -> { // in this block we must be sure that data is not null
                val weather = data.weather
                val hourly = weather?.data_hourly!!
                val todays = weather?.data_today!!
                val current = weather?.data_current!!

                runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                    binding.wholeLayout.visibility = View.VISIBLE

                    binding.timeZone.text = weather.data_timezone!!.timezone
                    binding.forecastText.text = weather.data_timezone!!.timezone + " " + binding.forecastText.text
                    fillCurrentWeather(current)
                    fillTodaysData(todays)
                    generateHourlyForecastCards(binding.hourlyForecast, hourly)
                }
            }

            State.ERROR -> {
                binding.wholeLayout.visibility = View.GONE
                //todo: show the reload button if location sharing is accepted but not data could be retreived
                //show the give location permission button if location sharing is not accepted
            }
        }
    }

    private fun fillCurrentWeather(currentWeather: WeatherDataCurrent) {
        binding.currentTemperature.text = currentWeather.temperature
        //binding.currentWindSpeed.text = currentWeather.windSpeed.toString()
        val weatherType = WeatherType.fromWMO(currentWeather.weather_code)
        binding.currentWeatherBigIcon.setImageResource(weatherType.iconRes)
        binding.weatherDescription.text = weatherType.weatherDesc
        binding.wholeLayout.setBackgroundColor(weatherType.backgroundColor)

        //to align background colors

    }

    private fun fillTodaysData(daily: WeatherDataToday) {
        binding.maxTemperature.text = daily.max_temperature
        binding.minTemperature.text = daily.min_temperature
    }

    private fun generateHourlyForecastCards(recyclerView: RecyclerView, hourly: WeatherDataHourly) {
        recyclerView.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false);
            adapter = HourlyWeatherRecyclerViewAdapter(hourly)
        }
        recyclerView.visibility = View.VISIBLE
    }
}