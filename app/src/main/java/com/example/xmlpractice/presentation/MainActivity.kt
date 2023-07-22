package com.example.xmlpractice.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xmlpractice.presentation.adapters.HourlyWeatherRecyclerViewAdapter
import com.example.xmlpractice.databinding.ActivityMainBinding
import com.example.xmlpractice.WeatherAppViewModel.IWeatherService
import com.example.xmlpractice.WeatherAppViewModel.LocationListener
import com.example.xmlpractice.WeatherAppViewModel.LocationManager
import com.example.xmlpractice.WeatherAppViewModel.State
import com.example.xmlpractice.WeatherAppViewModel.Weather
import com.example.xmlpractice.WeatherAppViewModel.WeatherDataCurrent
import com.example.xmlpractice.WeatherAppViewModel.WeatherDataHourly
import com.example.xmlpractice.WeatherAppViewModel.WeatherDataToday
import com.example.xmlpractice.WeatherAppViewModel.WeatherService
import com.example.xmlpractice.WeatherAppViewModel.WeatherStateListener
import com.example.xmlpractice.WeatherAppViewModel.WeatherType
import com.example.xmlpractice.WeatherAppViewModel.hasLocationPermission
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity :
    AppCompatActivity(),
    WeatherStateListener,
    LocationListener {

    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var m_binding: ActivityMainBinding
    private lateinit var m_data: Weather
    private lateinit var m_service: IWeatherService
    private lateinit var m_locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        m_binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(m_binding.root)

        m_service = WeatherService()
        m_locationManager = LocationManager(applicationContext)

        m_locationManager.setLocationListener(this)
        m_service.setWeatherStateListener(this)

        val activityResultLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                // Handle Permission granted/rejected
                permissions.entries.forEach {
                    val permissionName = it.key
                    val isGranted = it.value
                    if (isGranted) {
                        m_locationManager.startLocationUpdates()
                    } else {
                        onPermissionDenied()
                    }
                }
            }

        if (applicationContext.hasLocationPermission()) {
            //there is already a permission -> start location retrieval
            m_locationManager.startLocationUpdates()

        } else {
            //no permissions -> ask
            activityResultLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        m_locationManager.stopLocationUpdates()
    }

    //todo add onResume and check whether location is still granted, do the same on every start
    override fun onWeatherStateChanged(state: State) {
        when (state) {
            State.LOADING -> {
                runOnUiThread {
                    hideLayout()
                    m_binding.progressBar.visibility = View.VISIBLE
                }
            }

            State.SUCCESS -> { // in this block we must be sure that data is not null
                val weather = m_data.weather
                val hourly = weather?.data_hourly!!
                val todays = weather?.data_today!!
                val current = weather?.data_current!!

                runOnUiThread {
                    m_binding.progressBar.visibility = View.GONE
                    showLayout()

                    m_binding.timeZone.text = weather.data_timezone!!.timezone
                    m_binding.forecastText.text =
                        weather.data_timezone!!.timezone //todo: check the warning
                    fillCurrentWeather(current)
                    fillTodaysData(todays)
                    generateHourlyForecastCards(m_binding.hourlyForecast, hourly)
                }
            }

            State.ERROR -> {
                hideLayout()
                //todo: show the reload button if location sharing is accepted but not data could be retreived
                //show the give location permission button if location sharing is not accepted
            }
        }
    }

    private fun fillCurrentWeather(currentWeather: WeatherDataCurrent) {
        m_binding.currentTemperature.text = currentWeather.temperature
        //binding.currentWindSpeed.text = currentWeather.windSpeed.toString()
        val weatherType = WeatherType.fromWMO(currentWeather.weather_code)
        m_binding.currentWeatherBigIcon.setImageResource(weatherType.iconRes)
        m_binding.weatherDescription.text = weatherType.weatherDesc
        m_binding.wholeLayout.setBackgroundColor(weatherType.backgroundColor)

        //to align background colors

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

    private fun tryRequestWheatherData() {

    }

    private fun hideLayout() {
        m_binding.wholeLayout.visibility = View.GONE
    }

    private fun showLayout() {
        m_binding.wholeLayout.visibility = View.VISIBLE
    }

    //location
    override fun onLocationUpdate(latitude: Double, longitude: Double) {
        m_data = m_service.getWeatherData(latitude, longitude)
    }

    override fun onPermissionDenied() {
        hideLayout()
        //todo: show grant location button
    }
}