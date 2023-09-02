package com.example.weather_app_xml.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_app_xml.R
import com.example.weather_app_xml.WeatherAppViewModel.Constants
import com.example.weather_app_xml.WeatherAppViewModel.WeatherDataDaily
import com.example.weather_app_xml.WeatherAppViewModel.WeatherDataToday
import com.example.weather_app_xml.WeatherAppViewModel.WeatherType

class DailyWeatherRecyclerViewAdapter(val data: WeatherDataDaily) :
    RecyclerView.Adapter<WeatherDailyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherDailyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.daily_rectangle, parent, false)
        return WeatherDailyHolder(view)
    }

    override fun getItemCount(): Int {
        return Constants.forecastDays
    }

    override fun onBindViewHolder(holder: WeatherDailyHolder, position: Int) {
        return holder.bindView(data, position)
    }
}

class WeatherDailyHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
{
    private val date: TextView = itemView.findViewById(R.id.date)
    private val icon: ImageView = itemView.findViewById(R.id.weatherTypeIcon)
    private val temperatureLow: TextView = itemView.findViewById(R.id.lowestTemperature)
    private val temperatureHigh: TextView = itemView.findViewById(R.id.highestTemperature)

    fun bindView(data: WeatherDataDaily?, position: Int) {
        data?.let {
            date.text = data.dates[position]
            temperatureHigh.text = data.highest_temps[position]
            temperatureLow.text = data.lowest_temps[position]

            val weatherType = WeatherType.fromWMO(data.weather_codes[position])
            icon.setImageResource(weatherType.iconRes)
        }
    }
}