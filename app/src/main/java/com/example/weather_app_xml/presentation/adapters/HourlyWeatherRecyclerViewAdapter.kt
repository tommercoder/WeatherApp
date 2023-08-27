package com.example.weather_app_xml.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_app_xml.R
import com.example.weather_app_xml.WeatherAppViewModel.Constants
import com.example.weather_app_xml.WeatherAppViewModel.WeatherDataHourly
import com.example.weather_app_xml.data.helpers.TimeFormatter
import com.example.weather_app_xml.WeatherAppViewModel.WeatherType
//must be reworked to local types
class HourlyWeatherRecyclerViewAdapter(val data: WeatherDataHourly) : //use HourlyDto instead?
    RecyclerView.Adapter<WeatherSmallBoxHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherSmallBoxHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_small_box, parent, false)
        return WeatherSmallBoxHolder(view)
    }

    override fun getItemCount(): Int {
        return Constants.dayHours // 24
    }

    override fun onBindViewHolder(holder: WeatherSmallBoxHolder, position: Int) {
        return holder.bindView(data, position)
    }
}

class WeatherSmallBoxHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
{
    private val hour: TextView = itemView.findViewById(R.id.hour)
    private val icon: ImageView = itemView.findViewById(R.id.icon)
    private val temperature: TextView = itemView.findViewById(R.id.temperature)

    fun bindView(data: WeatherDataHourly?, position: Int) {
        data?.let {
            hour.text = TimeFormatter.getFormattedTimeForHourlyCard(data.hours[position])//move to mapper
            temperature.text = data.temperatures[position]

            val weatherType = WeatherType.fromWMO(data.weather_codes[position])
            icon.setImageResource(weatherType.iconRes)
        }
    }
}