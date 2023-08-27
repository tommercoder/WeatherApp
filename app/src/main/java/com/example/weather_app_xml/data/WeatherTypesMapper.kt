package com.example.weather_app_xml.data

import android.util.Log
import com.example.weather_app_xml.WeatherAppViewModel.Constants
import com.example.weather_app_xml.WeatherAppViewModel.WeatherDataCurrent
import com.example.weather_app_xml.WeatherAppViewModel.WeatherDataHolder
import com.example.weather_app_xml.WeatherAppViewModel.WeatherDataHourly
import com.example.weather_app_xml.WeatherAppViewModel.WeatherDataTimezone
import com.example.weather_app_xml.WeatherAppViewModel.WeatherDataToday
import com.example.weather_app_xml.data.remote.CurrentWeatherDataDto
import com.example.weather_app_xml.data.remote.DailyWeatherDataDto
import com.example.weather_app_xml.data.remote.HourlyWeatherDataDto
import com.example.weather_app_xml.data.remote.WeatherDto
import java.util.Calendar

class WeatherTypesMapper {

    fun mapDTOsToWeatherData(weatherDto: WeatherDto): WeatherDataHolder {
        return WeatherDataHolder(
            mapHourly(weatherDto.hourlyWeatherData),
            mapTodays(weatherDto.dailyWeatherData),
            mapCurrent(weatherDto.currentWeather),
            WeatherDataTimezone(weatherDto.timeZone)
        )
    }

    private fun doubleTemperaturesToString(temperatures: List<Double>): List<String> {
        return temperatures.map { "${it.toInt()}${ Constants.degreeSign }" }
    }

    private fun mapHourly(hourlyFromAPI: HourlyWeatherDataDto): WeatherDataHourly {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val hoursApi = hourlyFromAPI.time

        val hoursShift = currentHour + Constants.dayHours + 1
        val displayedHours: List<String> =
            hoursApi.subList(currentHour, hoursShift)

        return WeatherDataHourly(
            hours = displayedHours,
            temperatures = doubleTemperaturesToString(hourlyFromAPI.temperatures.subList(currentHour, hoursShift)),
            weather_codes = hourlyFromAPI.weatherCodes.subList(currentHour, hoursShift)
        )
    }

    private fun mapTodays(todayFromAPI: DailyWeatherDataDto): WeatherDataToday {
        return WeatherDataToday(
            max_temperature = "${todayFromAPI.todaysMaxTemperature[0].toInt()}${Constants.degreeSign}", // today's max temperature
            min_temperature = "${todayFromAPI.todaysMinTemperature[0].toInt()}${Constants.degreeSign}" // today's min temperature
        )
    }

    private fun mapCurrent(currentFromAPI: CurrentWeatherDataDto): WeatherDataCurrent {
        return WeatherDataCurrent(
            temperature = "${currentFromAPI.temperature.toInt()}${Constants.degreeSign}",
            weather_code = currentFromAPI.weatherCode,
            wind_speed = currentFromAPI.windSpeed.toString()
        )
    }
}