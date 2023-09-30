package com.example.weather_app_xml.data

import com.example.weather_app_xml.WeatherAppViewModel.Constants
import com.example.weather_app_xml.WeatherAppViewModel.WeatherDataCurrent
import com.example.weather_app_xml.WeatherAppViewModel.WeatherDataDaily
import com.example.weather_app_xml.WeatherAppViewModel.WeatherDataHolder
import com.example.weather_app_xml.WeatherAppViewModel.WeatherDataHourly
import com.example.weather_app_xml.WeatherAppViewModel.WeatherDataTimezone
import com.example.weather_app_xml.WeatherAppViewModel.WeatherDataToday
import com.example.weather_app_xml.WeatherAppViewModel.checkAndReturnSublist
import com.example.weather_app_xml.data.helpers.TimeFormatter
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
            WeatherDataTimezone(weatherDto.timeZone),
            mapDaily(weatherDto.dailyWeatherData)
        )
    }

    private fun doubleTemperaturesToString(temperatures: List<Double>): List<String> {
        return temperatures.map { "${it.toInt()}${Constants.degreeSign}" }
    }

    private fun mapHourly(hourlyFromAPI: HourlyWeatherDataDto): WeatherDataHourly { //get whole list(sub lists are done when using)
        return WeatherDataHourly(
            hours = hourlyFromAPI.time,
            temperatures = doubleTemperaturesToString(hourlyFromAPI.temperatures),
            weather_codes = hourlyFromAPI.weatherCodes
        )
    }

    private fun mapDaily(dailyFromAPI: DailyWeatherDataDto): WeatherDataDaily {
        val startPosition = 0
        val endPosition = Constants.forecastDays
        return WeatherDataDaily(
            dates = checkAndReturnSublist(startPosition, endPosition, dailyFromAPI.dates),
            highest_temps = doubleTemperaturesToString(checkAndReturnSublist(startPosition, endPosition,dailyFromAPI.maxTemperatures)),
            lowest_temps = doubleTemperaturesToString(checkAndReturnSublist(startPosition, endPosition,dailyFromAPI.minTemperatures)),
            weather_codes = checkAndReturnSublist(startPosition, endPosition, dailyFromAPI.weatherCodes)
        )
    }

    private fun mapTodays(todayFromAPI: DailyWeatherDataDto): WeatherDataToday {
        return WeatherDataToday(
            max_temperature = "${todayFromAPI.maxTemperatures[0].toInt()}${Constants.degreeSign}", // today's max temperature
            min_temperature = "${todayFromAPI.minTemperatures[0].toInt()}${Constants.degreeSign}" // today's min temperature
        )
    }

    private fun mapCurrent(currentFromAPI: CurrentWeatherDataDto): WeatherDataCurrent {
        return WeatherDataCurrent(
            current_hour = TimeFormatter.getHourFromAPITimeString(currentFromAPI.time),
            temperature = "${currentFromAPI.temperature.toInt()}${Constants.degreeSign}",
            weather_code = currentFromAPI.weatherCode,
            wind_speed = currentFromAPI.windSpeed.toString()
        )
    }

}