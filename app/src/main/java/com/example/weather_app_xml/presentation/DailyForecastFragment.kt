package com.example.weather_app_xml.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weather_app_xml.R
import com.example.weather_app_xml.WeatherAppViewModel.Constants
import com.example.weather_app_xml.WeatherAppViewModel.WeatherDataDaily
import com.example.weather_app_xml.WeatherAppViewModel.WeatherDataHolder
import com.example.weather_app_xml.WeatherAppViewModel.WeatherType
import com.example.weather_app_xml.WeatherAppViewModel.checkAndReturnSublist
import com.example.weather_app_xml.databinding.DatailedDailyForecastBinding
import com.example.weather_app_xml.databinding.DatailedDailyForecastBinding.inflate
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DailyForecastFragment(data: WeatherDataHolder, position: Int) : BottomSheetDialogFragment() {
    private lateinit var binding: DatailedDailyForecastBinding
    private val mdata = data
    private val mposition = position
    private val mdaily = data.data_daily!!
    private val mhourly = data.data_hourly!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflate(inflater, container, false)

        binding.bigIconImageView.setImageResource(WeatherType.fromWMO(mdaily.weather_codes[mposition]).iconRes)
        binding.minTemperatureTextView.text = mdaily.lowest_temps[mposition]
        binding.maxTemperatureTextView.text = mdaily.highest_temps[mposition]
        binding.dataTextView.text = mdaily.dates[mposition]

        val startPosition: Int = mposition * Constants.forecastHours
        val endPosition: Int = startPosition + Constants.forecastHours
        var forecastTemps: List<String> = checkAndReturnSublist(startPosition, endPosition, mhourly.temperatures)

        //forecastTe

        //draw a chart below
        //hours on the left and temperatures on the left?
        //take every 4th hour for example from the list of temperatures
        //1)how to get list of temperature for a whole specific day? -> get it from the Hourly? take start as selected day*24 and then + 24 more hours
        //2)how to draw a chart?

        return binding.root
    }
}