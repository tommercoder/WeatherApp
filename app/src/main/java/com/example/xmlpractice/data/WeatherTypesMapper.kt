package com.example.xmlpractice.data

import com.example.xmlpractice.WeatherAppViewModel.Constants
import com.example.xmlpractice.WeatherAppViewModel.WeatherDataCurrent
import com.example.xmlpractice.WeatherAppViewModel.WeatherDataHolder
import com.example.xmlpractice.WeatherAppViewModel.WeatherDataHourly
import com.example.xmlpractice.WeatherAppViewModel.WeatherDataTimezone
import com.example.xmlpractice.WeatherAppViewModel.WeatherDataToday
import com.example.xmlpractice.data.remote.CurrentWeatherDataDto
import com.example.xmlpractice.data.remote.DailyWeatherDataDto
import com.example.xmlpractice.data.remote.HourlyWeatherDataDto
import com.example.xmlpractice.data.remote.WeatherDto
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
        return temperatures.map { "${it.toInt()}${Constants.degreeSign}" } // maybe degree sign should be added somewhere else?
    }

    private fun mapHourly(hourlyFromAPI: HourlyWeatherDataDto): WeatherDataHourly {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR)
        val hoursApi = hourlyFromAPI.time

        val displayedHours: List<String> =
            hoursApi.subList(currentHour, currentHour + Constants.dayHours + 1)//move to const

        return WeatherDataHourly(
            hours = displayedHours,
            temperatures = doubleTemperaturesToString(hourlyFromAPI.temperatures),
            weather_codes = hourlyFromAPI.weatherCodes
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
            wind_speed = currentFromAPI.windSpeed
        )
    }
}