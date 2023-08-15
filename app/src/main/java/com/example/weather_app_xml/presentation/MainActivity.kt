package com.example.weather_app_xml.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_app_xml.presentation.adapters.HourlyWeatherRecyclerViewAdapter
import com.example.weather_app_xml.databinding.ActivityMainBinding
import com.example.weather_app_xml.WeatherAppViewModel.WeatherDataCurrent
import com.example.weather_app_xml.WeatherAppViewModel.WeatherDataHourly
import com.example.weather_app_xml.WeatherAppViewModel.WeatherDataToday
import com.example.weather_app_xml.WeatherAppViewModel.WeatherType
import android.Manifest
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.weather_app_xml.WeatherAppViewModel.MainViewModel
import com.example.weather_app_xml.WeatherAppViewModel.State
import com.example.weather_app_xml.WeatherAppViewModel.Weather
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var m_binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        m_binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(m_binding.root)

        val activityResultLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                // Handle Permission granted/rejected
                permissions.entries.forEach {
                    val permissionName = it.key
                    val isGranted = it.value
                    if (isGranted) {
                        viewModel.loadWeather()
                        viewModel.liveData.observe(this, Observer { newState ->
                            when (newState) {
                                State.SUCCESS -> {
                                    setupUI(viewModel.weatherData!!) // must be != null here
                                }

                                State.ERROR -> {
                                    runOnUiThread {
                                        hideProgressBar()
                                    }
                                    //todo: show the reload button if location sharing is accepted but not data could be retreived
                                }

                                State.LOADING -> {
                                    runOnUiThread {
                                        showProgressBar() // check if works
                                    }
                                }

                                else -> {
                                    //mustn't happen
                                }
                            }
                        })
                    } else {
                        //todo: show the button to ask again for location
                    }
                }
            }

        activityResultLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

    }

    private fun showProgressBar() {
        runOnUiThread {
            hideLayout()
            m_binding.progressBar.visibility = View.VISIBLE
        }
    }

    private fun hideProgressBar() {
        runOnUiThread {
            showLayout()
            m_binding.progressBar.visibility = View.GONE
        }
    }

    private fun setupUI(weather: Weather) {
        val info = weather.weather
        val hourly = info?.data_hourly!!
        val todays = info?.data_today!!
        val current = info?.data_current!!

        runOnUiThread {
            m_binding.progressBar.visibility = View.GONE
            showLayout()

            m_binding.timeZone.text = info.data_timezone!!.timezone
            m_binding.forecastText.text =
                info.data_timezone!!.timezone //todo: check the warning
            fillCurrentWeather(current)
            fillTodaysData(todays)
            generateHourlyForecastCards(m_binding.hourlyForecast, hourly)
        }
    }
    //todo add onResume and check whether location is still granted, do the same on every start

    private fun fillCurrentWeather(currentWeather: WeatherDataCurrent) {
        m_binding.currentTemperature.text = currentWeather.temperature
        //binding.currentWindSpeed.text = currentWeather.windSpeed.toString()
        val weatherType = WeatherType.fromWMO(currentWeather.weather_code)
        m_binding.currentWeatherBigIcon.setImageResource(weatherType.iconRes)
        m_binding.weatherDescription.text = weatherType.weatherDesc
        m_binding.wholeLayout.setBackgroundColor(weatherType.backgroundColor)
    }

    private fun fillTodaysData(daily: WeatherDataToday) {
        m_binding.maxTemperature.text = daily.max_temperature
        m_binding.minTemperature.text = daily.min_temperature
    }

    private fun generateHourlyForecastCards(recyclerView: RecyclerView, hourly: WeatherDataHourly) {
        recyclerView.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false);
            adapter = HourlyWeatherRecyclerViewAdapter(hourly)
        }
        recyclerView.visibility = View.VISIBLE
    }
    private fun hideLayout() {
        m_binding.wholeLayout.visibility = View.GONE
    }

    private fun showLayout() {
        m_binding.wholeLayout.visibility = View.VISIBLE
    }
}