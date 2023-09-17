package com.example.weather_app_xml.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_app_xml.R
import com.example.weather_app_xml.WeatherAppViewModel.MainViewModel
import com.example.weather_app_xml.WeatherAppViewModel.State
import com.example.weather_app_xml.WeatherAppViewModel.Weather
import com.example.weather_app_xml.WeatherAppViewModel.WeatherDataCurrent
import com.example.weather_app_xml.WeatherAppViewModel.WeatherDataDaily
import com.example.weather_app_xml.WeatherAppViewModel.WeatherDataHolder
import com.example.weather_app_xml.WeatherAppViewModel.WeatherDataHourly
import com.example.weather_app_xml.WeatherAppViewModel.WeatherDataTimezone
import com.example.weather_app_xml.WeatherAppViewModel.WeatherDataToday
import com.example.weather_app_xml.WeatherAppViewModel.WeatherType
import com.example.weather_app_xml.WeatherAppViewModel.hasLocationPermission
import com.example.weather_app_xml.databinding.ActivityMainBinding
import com.example.weather_app_xml.presentation.adapters.DailyWeatherRecyclerViewAdapter
import com.example.weather_app_xml.presentation.adapters.HourlyWeatherRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var activityResultLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        //todo: check what to do with the deny forever case!!!
        //location permission result launcher
        activityResultLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) {
                if (hasLocationPermissions()) {
                    Log.d("TAG", "trigger loadWEather")
                    viewModel.loadWeather()
                } else {
                    Log.d("TAG", "no location permissions")
                    showInfoText(R.string.reload_location)
                }
            }

        //observe the activity swipe to refresh
        binding.refreshLayout.setOnRefreshListener {
            //hide internal progress bar?
            if (hasLocationPermissions()) {
                viewModel.loadWeather()

            } else {
                requestLocationPermissions()
            }
            binding.refreshLayout.isRefreshing = true
        }

        //observe the change of the API request state for current location
        viewModel.liveData.observe(this, Observer { newState ->
            when (newState) {
                State.SUCCESS -> {
                    hideProgressBar()
                    hideInfoText()
                    binding.refreshLayout.isRefreshing = false
                    setupUI(viewModel.weatherData!!) // must be != null here
                    onBtnHourlyClicked() // default
                }

                State.ERROR -> {
                    hideProgressBar()
                    binding.refreshLayout.isRefreshing = false
                    showInfoText(R.string.reload_weather)
                }

                State.LOADING -> {
                    hideInfoText()
                    if (!binding.refreshLayout.isRefreshing) { //prevent progress bar visibility when everything is visible
                        showProgressBar()
                    }
                    binding.refreshLayout.isRefreshing = false
                }

                else -> {
                    hideProgressBar()
                    binding.refreshLayout.isRefreshing = false
                    showInfoText(R.string.something_went_wrong)
                    //mustn't happen
                }
            }
        })

        requestLocationPermissions()
        setupButtons()
    }

    private fun hasLocationPermissions(): Boolean {
        return application.applicationContext.hasLocationPermission()
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
            binding.progressBar.visibility = View.VISIBLE
        }
    }

    private fun hideProgressBar() {
        runOnUiThread {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showInfoText(id: Int) {
        runOnUiThread {
            binding.infoText.setText(resources.getString(id))
            binding.infoText.visibility = View.VISIBLE
        }
    }

    private fun hideInfoText() {
        runOnUiThread {
            binding.infoText.visibility = View.GONE
        }
    }

    private fun setupUI(weather: Weather) {
        val info = weather.weather
        val hourly = info?.data_hourly!!
        val daily = info?.data_daily!!
        val todays = info?.data_today!!
        val current = info?.data_current!!
        val time_zone = info?.data_timezone!!

        runOnUiThread {
            hideProgressBar()
            showLayout()

            fillTimeZone(time_zone)
            fillCurrentWeather(current)
            fillTodaysData(todays)
            generateHourlyForecastCards(binding.hourlyForecast, hourly)
            generateDailyForecastCards(binding.dailyForecast, info)
        }
    }

    private fun fillTimeZone(zone: WeatherDataTimezone) {
        binding.timeZone.text = zone.timezone
        binding.forecastText.text =
            zone.timezone + " " + resources.getString(R.string.forecast_text) //todo: check the warning
    }

    private fun fillCurrentWeather(currentWeather: WeatherDataCurrent) {
        runOnUiThread {
            binding.currentTemperature.text = currentWeather.temperature
            binding.windSpeedLayout.windSpeed.text = currentWeather.wind_speed
            val weatherType = WeatherType.fromWMO(currentWeather.weather_code)
            binding.currentWeatherBigIcon.setImageResource(weatherType.iconRes)
            binding.weatherDescription.text = weatherType.weatherDesc
            binding.refreshLayout.setBackgroundResource(weatherType.fullscreenImage)
        }
    }

    private fun fillTodaysData(daily: WeatherDataToday) {
        runOnUiThread {
            binding.maxTemperature.text = daily.max_temperature
            binding.minTemperature.text = daily.min_temperature
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

    private fun generateDailyForecastCards(
        recyclerView: RecyclerView,
        data: WeatherDataHolder
    ) {
        runOnUiThread {
            //setup on click
            var _adapter = DailyWeatherRecyclerViewAdapter(data.data_daily!!)
            _adapter.setOnItemClickListener(object :
                DailyWeatherRecyclerViewAdapter.onItemClickListener {
                override fun onItemClick(position: Int) {
                    onDailyForecastClicked(data, position)
                }
            })

            recyclerView.apply {
                layoutManager =
                    LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false);
                adapter = _adapter
                visibility = View.VISIBLE
            }
        }
    }

    private fun onDailyForecastClicked(data: WeatherDataHolder, position: Int){
        //implement opening of a different fragment with some data
        Log.d("TAG", position.toString())

        val d = DailyForecastFragment(data, position)
        d.show(supportFragmentManager, "")
    }

    private fun hideLayout() {
        runOnUiThread {
            binding.refreshLayout.visibility = View.INVISIBLE
        }
    }

    private fun showLayout() {
        runOnUiThread {
            binding.refreshLayout.visibility = View.VISIBLE
            binding.multipleViewsLayout.visibility = View.VISIBLE
        }
    }

    private fun setupButtons() {
        binding.btnHourly.setOnClickListener {
            onBtnHourlyClicked()
        }

        binding.btnDaily.setOnClickListener {
            onBtnDailyClicked()
        }
    }

    private fun onBtnHourlyClicked() {
        binding.btnDaily.isSelected = false
        binding.btnHourly.isSelected = true

        binding.hourlyForecast.visibility = View.VISIBLE
        binding.windSpeedLayout.root.visibility = View.VISIBLE
        binding.dailyForecast.visibility = View.GONE
    }

    private fun onBtnDailyClicked() {
        binding.btnHourly.isSelected = false
        binding.btnDaily.isSelected = true

        binding.hourlyForecast.visibility = View.INVISIBLE
        binding.windSpeedLayout.root.visibility = View.GONE
        binding.dailyForecast.visibility = View.VISIBLE
    }
}