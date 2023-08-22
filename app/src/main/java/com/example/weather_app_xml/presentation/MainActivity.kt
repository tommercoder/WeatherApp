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
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.weather_app_xml.R
import com.example.weather_app_xml.WeatherAppViewModel.MainViewModel
import com.example.weather_app_xml.WeatherAppViewModel.State
import com.example.weather_app_xml.WeatherAppViewModel.Weather
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var m_binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var activityResultLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        m_binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(m_binding.root)

        //todo: check what to do with the deny forever case!!!
        //location permission result launcher
        activityResultLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) {
                if (hasLocationPermissions()) {
                    viewModel.loadWeather()
                } else {
                    showInfoText(R.string.reload_location)
                }
            }

        //observe the activity swipe to refresh
        m_binding.refreshLayout.setOnRefreshListener {
            if (hasLocationPermissions()) {
                viewModel.loadWeather()
            } else {
                requestLocationPermissions()
            }

            m_binding.refreshLayout.isRefreshing = false // stop the refreshing
        }

        //observe the change of the API request state for current location
        viewModel.liveData.observe(this, Observer { newState ->
            when (newState) {
                State.SUCCESS -> {
                    hideProgressBar()
                    hideInfoText()
                    setupUI(viewModel.weatherData!!) // must be != null here
                }

                State.ERROR -> {
                    hideProgressBar()
                    showInfoText(R.string.reload_weather) //todo: fix the crash!!!
                }

                State.LOADING -> {
                    hideInfoText()
                    //if (!m_binding.refreshLayout.isVisible) { //prevent progress bar visibility when everything is visible
                        showProgressBar()
                   // }
                }

                else -> {
                    //mustn't happen
                }
            }
        })

        requestLocationPermissions()
    }

    private fun hasLocationPermissions(): Boolean {
        val fineLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        val coarseLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        return fineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                coarseLocationPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermissions() {
        hideProgressBar()
        hideInfoText()
        if (::activityResultLauncher.isInitialized) {
            activityResultLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                )
            )
        }
    }

    private fun showProgressBar() {
        runOnUiThread {
            //hideLayout()
            m_binding.progressBar.visibility = View.VISIBLE
        }
    }

    private fun hideProgressBar() {
        runOnUiThread {
            //showLayout()
            m_binding.progressBar.visibility = View.GONE
        }
    }

    private fun showInfoText(id: Int) {
        runOnUiThread {
            m_binding.infoText.setText(resources.getString(id))
            m_binding.infoText.visibility = View.VISIBLE
        }
    }

    private fun hideInfoText() {
        runOnUiThread {
            m_binding.infoText.visibility = View.GONE
        }
    }

    private fun setupUI(weather: Weather) {
        val info = weather.weather
        val hourly = info?.data_hourly!!
        val todays = info?.data_today!!
        val current = info?.data_current!!

        runOnUiThread {
            hideProgressBar()
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
        m_binding.refreshLayout.setBackgroundColor(weatherType.backgroundColor)
    }

    private fun fillTodaysData(daily: WeatherDataToday) {
        m_binding.maxTemperature.text = daily.max_temperature
        m_binding.minTemperature.text = daily.min_temperature
    }

    private fun generateHourlyForecastCards(
        recyclerView: RecyclerView,
        hourly: WeatherDataHourly
    ) {
        recyclerView.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false);
            adapter = HourlyWeatherRecyclerViewAdapter(hourly)
        }
        recyclerView.visibility = View.VISIBLE
    }

    private fun hideLayout() {
        m_binding.refreshLayout.visibility = View.GONE
    }

    private fun showLayout() {
        m_binding.refreshLayout.visibility = View.VISIBLE
    }
}