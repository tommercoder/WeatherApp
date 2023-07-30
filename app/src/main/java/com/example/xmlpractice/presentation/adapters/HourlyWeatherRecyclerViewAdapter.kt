package com.example.xmlpractice.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.xmlpractice.R
import com.example.xmlpractice.WeatherAppViewModel.WeatherDataHourly
import com.example.xmlpractice.data.helpers.TimeFormatter
import com.example.xmlpractice.WeatherAppViewModel.WeatherType
//must be reworked to local types
class HourlyWeatherRecyclerViewAdapter(val data: WeatherDataHourly) : //use HourlyDto instead?
    RecyclerView.Adapter<WeatherSmallBoxHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherSmallBoxHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_small_box, parent, false)
        return WeatherSmallBoxHolder(view)
    }

    override fun getItemCount(): Int {
        return 24 // hours
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
    //private val cardLayout: LinearLayout = itemView.findViewById(R.id.card)
    //todo: highlight last selected? show some additional data for the selected element?
    fun bindView(data: WeatherDataHourly?, position: Int) {
        data?.let {
            hour.text = TimeFormatter.getFormattedTimeForHourlyCard(data.hours[position])//move to mapper
            temperature.text = data.temperatures[position]
            val weatherType = WeatherType.fromWMO(data.weather_codes[position])
            icon.setImageResource(weatherType.iconRes)
            //cardLayout.setBackgroundColor("#00ffffff")
        }
    }
}