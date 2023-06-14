package com.example.xmlpractice.presentation

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xmlpractice.data.remote.CurrentWeatherDataDto
import com.example.xmlpractice.data.remote.DailyWeatherDataDto
import com.example.xmlpractice.presentation.adapters.HourlyWeatherRecyclerViewAdapter
import com.example.xmlpractice.data.remote.WeatherDto
import com.example.xmlpractice.databinding.ActivityMainBinding
import com.example.xmlpractice.WeatherAppViewModel.AppModule
import com.example.xmlpractice.WeatherAppViewModel.IWeatherService
import com.example.xmlpractice.WeatherAppViewModel.State
import com.example.xmlpractice.WeatherAppViewModel.Weather
import com.example.xmlpractice.WeatherAppViewModel.WeatherDataCurrent
import com.example.xmlpractice.WeatherAppViewModel.WeatherDataToday
import com.example.xmlpractice.WeatherAppViewModel.WeatherService
import com.example.xmlpractice.WeatherAppViewModel.WeatherStateListener
import com.example.xmlpractice.WeatherAppViewModel.WeatherType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity(), WeatherStateListener {
    private lateinit var binding: ActivityMainBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HourlyWeatherRecyclerViewAdapter

    private val service: IWeatherService = WeatherService()
    private lateinit var data: Weather

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        service.setWeatherStateListener(this)
        data = service.getWeatherData()

//
//        for (a in data.weather?.hourly!!.temperatures) {
//            Log.d("TAG", a);
//            Log.d("AAA", data.current_state.toString())
//        }
    }

    override fun onWeatherStateChanged(state: State) {
        when (state) {
            State.LOADING -> {
                runOnUiThread {
                    binding.wholeLayout.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
            State.SUCCESS -> {

                val weather = data.weather
                val hourly = weather?.hourly
                val todays = weather?.today
                val current = weather?.current


                runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                    binding.wholeLayout.visibility = View.VISIBLE

                    fillCurrentWeather(current!!)
                    fillTodaysData(todays!!)
                }
            }
            State.ERROR -> {
            }
        }
    }

    private fun fillCurrentWeather(currentWeather: WeatherDataCurrent){
        binding.currentTemperature.text = currentWeather.temperature
        //binding.currentWindSpeed.text = currentWeather.windSpeed.toString()
        val weatherType = WeatherType.fromWMO(currentWeather.weather_code)
        binding.currentWeatherBigIcon.setImageResource(weatherType.iconRes)
        binding.weatherDescription.text = weatherType.weatherDesc
        binding.wholeLayout.setBackgroundColor(weatherType.backgroundColor)
    }

    private fun fillTodaysData(daily : WeatherDataToday) {
        binding.maxTemperature.text = daily.max_temperature
        binding.minTemperature.text = daily.min_temperature
    }
//    private fun generateHourlyForecastCards(recyclerView: RecyclerView, weatherDto: WeatherDto) {
//        recyclerView.apply {
//            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false);
//            adapter = HourlyWeatherRecyclerViewAdapter(weatherDto)
//        }
//        recyclerView.visibility = View.VISIBLE
//    }
//
//
//    private fun fillTodaysData(daily : DailyWeatherDataDto) {
//        binding.maxTemperature.text = daily.todaysMaxTemperature[0].toString()
//        binding.minTemperature.text = daily.todaysMinTemperature[0].toString()
//    }

}