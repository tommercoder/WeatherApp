package com.example.weather_app_xml.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_app_xml.R
import com.example.weather_app_xml.WeatherAppViewModel.Constants
import com.example.weather_app_xml.WeatherAppViewModel.DetailedDailyWeather
import com.example.weather_app_xml.WeatherAppViewModel.WeatherDataDaily
import com.example.weather_app_xml.WeatherAppViewModel.WeatherDataHolder
import com.example.weather_app_xml.WeatherAppViewModel.WeatherType
import com.example.weather_app_xml.WeatherAppViewModel.checkAndReturnSublist
import com.example.weather_app_xml.databinding.DatailedDailyForecastBinding
import com.example.weather_app_xml.databinding.DatailedDailyForecastBinding.inflate
import com.example.weather_app_xml.presentation.adapters.HourlyWeatherRecyclerViewAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DailyForecastFragment(data: DetailedDailyWeather) : BottomSheetDialogFragment() {
    private lateinit var binding: DatailedDailyForecastBinding
    private val mdata = data

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflate(inflater, container, false)

        binding.bigIconImageView.setImageResource(WeatherType.fromWMO(mdata.daily_weather_code).iconRes)
        binding.minTemperatureTextView.text = mdata.daily_lowest_temp
        binding.maxTemperatureTextView.text = mdata.daily_highest_temp
        binding.dataTextView.text = mdata.daily_date
        binding.forecastDesc.text = WeatherType.fromWMO(mdata.daily_weather_code).weatherDesc

        binding.hourlyForecast.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            adapter = HourlyWeatherRecyclerViewAdapter(mdata.hourly!!)
        }
        return binding.root
    }
}