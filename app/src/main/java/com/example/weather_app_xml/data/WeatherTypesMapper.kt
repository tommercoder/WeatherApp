package com.example.weather_app_xml.data

import com.example.weather_app_xml.WeatherAppViewModel.Constants
import com.example.weather_app_xml.WeatherAppViewModel.WeatherDataCurrent
import com.example.weather_app_xml.WeatherAppViewModel.WeatherDataDaily
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
            WeatherDataTimezone(weatherDto.timeZone),
            mapDaily(weatherDto.dailyWeatherData)
        )
    }

    private fun doubleTemperaturesToString(temperatures: List<Double>): List<String> {
        return temperatures.map { "${it.toInt()}${Constants.degreeSign}" }
    }

    private fun mapHourly(hourlyFromAPI: HourlyWeatherDataDto): WeatherDataHourly {
        //API doesn't contain GMT+3, that's why current display data is different from the first hourly card
        //seems that should work on a real phone, not an the emulator
        //could be fixed by using the current_data time instead of Calendar
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val hoursApi = hourlyFromAPI.time

        val hoursShift = currentHour + Constants.forecastHours
        val displayedHours: List<String> =
            hoursApi.subList(currentHour, hoursShift)

        return WeatherDataHourly(
            hours = displayedHours,
            temperatures = doubleTemperaturesToString(
                hourlyFromAPI.temperatures.subList(
                    currentHour,
                    hoursShift
                )
            ),
            weather_codes = hourlyFromAPI.weatherCodes.subList(currentHour, hoursShift)
        )
    }

    private fun mapDaily(dailyFromAPI: DailyWeatherDataDto): WeatherDataDaily {
        val shift = 1 // start from tomorrow
        val last_position = Constants.forecastDays + shift // 11
        return WeatherDataDaily( // is there any better way to implement this?
            dates = dailyFromAPI.dates.subList(shift, last_position),
            highest_temps = doubleTemperaturesToString(
                dailyFromAPI.maxTemperatures.subList(
                    shift,
                    last_position
                )
            ),
            lowest_temps = doubleTemperaturesToString(
                dailyFromAPI.minTemperatures.subList(
                    shift,
                    last_position
                )
            ),
            weather_codes = dailyFromAPI.weatherCodes.subList(shift, last_position)
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
            temperature = "${currentFromAPI.temperature.toInt()}${Constants.degreeSign}",
            weather_code = currentFromAPI.weatherCode,
            wind_speed = currentFromAPI.windSpeed.toString()
        )
    }
}