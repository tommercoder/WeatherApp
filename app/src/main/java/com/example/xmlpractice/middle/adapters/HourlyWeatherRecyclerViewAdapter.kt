package com.example.xmlpractice.middle.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.xmlpractice.R
import com.example.xmlpractice.data.helpers.TimeFormatter
import com.example.xmlpractice.data.remote.HourlyWeatherDataDto
import com.example.xmlpractice.data.remote.WeatherDto
import com.example.xmlpractice.middle.WeatherType

class HourlyWeatherRecyclerViewAdapter(val data: WeatherDto) : //use HourlyDto instead?
    RecyclerView.Adapter<WeatherSmallBoxHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherSmallBoxHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_small_box, parent, false)
        return WeatherSmallBoxHolder(view)
    }

    override fun getItemCount(): Int {
        return data.hourlyWeatherData.temperatures.count() // looks bad
    }

    override fun onBindViewHolder(holder: WeatherSmallBoxHolder, position: Int) {
        return holder.bindView(data.hourlyWeatherData, position)
    }
}

class WeatherSmallBoxHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
{
    private val hour: TextView = itemView.findViewById(R.id.hour)
    private val icon: ImageView = itemView.findViewById(R.id.icon)
    private val temperature: TextView = itemView.findViewById(R.id.temperature)
    private val cardLayout: LinearLayout = itemView.findViewById(R.id.card)

    fun bindView(data: HourlyWeatherDataDto?, position: Int) {
        data?.let {//what is let?
            //start showing from current hour somehow?
            hour.text = TimeFormatter.getFormattedTimeForHourlyCard(data.time[position])
            temperature.text = data.temperatures[position].toString()
            val weatherType = WeatherType.fromWMO(data.weatherCode[position])
            icon.setImageResource(weatherType.iconRes)
            cardLayout.setBackgroundColor(weatherType.backgroundColor)
        }//test
    }
}