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
import com.example.weather_app_xml.WeatherAppViewModel.WeatherDataTimezone
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
                    onBtnHourlyClicked() // default
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
            m_binding.refreshLayout.isRefreshing = true
        }

        //observe the change of the API request state for current location
        viewModel.liveData.observe(this, Observer { newState ->
            when (newState) {
                State.SUCCESS -> {
                    hideProgressBar()
                    hideInfoText()
                    m_binding.refreshLayout.isRefreshing = false
                    setupUI(viewModel.weatherData!!) // must be != null here
                }

                State.ERROR -> {
                    hideProgressBar()
                    m_binding.refreshLayout.isRefreshing = false
                    showInfoText(R.string.reload_weather)
                }

                State.LOADING -> {
                    hideInfoText()
                    if (!m_binding.refreshLayout.isRefreshing) { //prevent progress bar visibility when everything is visible
                        showProgressBar()
                    }
                    m_binding.refreshLayout.isRefreshing = false
                }

                else -> {
                    //mustn't happen
                }
            }
        })

        requestLocationPermissions()
        setupButtons()
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
        //hideLayout()
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
            m_binding.progressBar.visibility = View.VISIBLE
        }
    }

    private fun hideProgressBar() {
        runOnUiThread {
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
        val time_zone = info?.data_timezone!!

        runOnUiThread {
            hideProgressBar()
            showLayout()

            fillTimeZone(time_zone)
            fillCurrentWeather(current)
            fillTodaysData(todays)
            generateHourlyForecastCards(m_binding.hourlyForecast, hourly)
        }
    }

    private fun fillTimeZone(zone: WeatherDataTimezone) {
        m_binding.timeZone.text = zone.timezone
        m_binding.forecastText.text =
            zone.timezone + " " + resources.getString(R.string.forecast_text) //todo: check the warning
    }

    private fun fillCurrentWeather(currentWeather: WeatherDataCurrent) {
        runOnUiThread {
            m_binding.currentTemperature.text = currentWeather.temperature
            m_binding.windSpeedLayout.windSpeed.text = currentWeather.wind_speed
            val weatherType = WeatherType.fromWMO(currentWeather.weather_code)
            m_binding.currentWeatherBigIcon.setImageResource(weatherType.iconRes)
            m_binding.weatherDescription.text = weatherType.weatherDesc
            m_binding.refreshLayout.setBackgroundResource(weatherType.fullscreenImage)
        }
    }

    private fun fillTodaysData(daily: WeatherDataToday) {
        runOnUiThread {
            m_binding.maxTemperature.text = daily.max_temperature
            m_binding.minTemperature.text = daily.min_temperature
        }
    }

    private fun generateHourlyForecastCards(
        recyclerView: RecyclerView,
        hourly: WeatherDataHourly
    ) {
        runOnUiThread {
            recyclerView.apply {
                layoutManager =
                    LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false);
                adapter = HourlyWeatherRecyclerViewAdapter(hourly)
            }
            recyclerView.visibility = View.VISIBLE
        }
    }

    private fun hideLayout() {
        runOnUiThread {
            m_binding.refreshLayout.visibility = View.INVISIBLE
        }
    }

    private fun showLayout() {
        runOnUiThread {
            m_binding.refreshLayout.visibility = View.VISIBLE
            m_binding.multipleViewsLayout.visibility = View.VISIBLE
        }
    }

    private fun setupButtons() {
        m_binding.btnHourly.setOnClickListener {
            onBtnHourlyClicked()
        }

        m_binding.btnDaily.setOnClickListener {
            onBtnDailyClicked()
        }
    }

    private fun onBtnHourlyClicked() {
        m_binding.btnDaily.isSelected = false
        m_binding.btnHourly.isSelected = true

        m_binding.hourlyForecast.visibility = View.VISIBLE
        m_binding.windSpeedLayout.root.visibility = View.VISIBLE
        //hide the daily forecast
    }

    private fun onBtnDailyClicked() {
        m_binding.btnHourly.isSelected = false
        m_binding.btnDaily.isSelected = true

        m_binding.hourlyForecast.visibility = View.INVISIBLE
        m_binding.windSpeedLayout.root.visibility = View.GONE
        //show the daily forecast
    }
}