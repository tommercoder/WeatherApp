package com.example.xmlpractice.data

import com.example.xmlpractice.WeatherAppViewModel.WeatherDataCurrent
import com.example.xmlpractice.WeatherAppViewModel.WeatherDataHolder
import com.example.xmlpractice.WeatherAppViewModel.WeatherDataHourly
import com.example.xmlpractice.WeatherAppViewModel.WeatherDataTimezone
import com.example.xmlpractice.WeatherAppViewModel.WeatherDataToday
import com.example.xmlpractice.data.remote.CurrentWeatherDataDto
import com.example.xmlpractice.data.remote.DailyWeatherDataDto
import com.example.xmlpractice.data.remote.HourlyWeatherDataDto
import com.example.xmlpractice.data.remote.WeatherDto

class WeatherTypesMapper {
    public fun mapDTOsToWeatherData(weatherDto: WeatherDto): WeatherDataHolder {
        return WeatherDataHolder(
            mapHourly(weatherDto.hourlyWeatherData),
            mapTodays(weatherDto.dailyWeatherData),
            mapCurrent(weatherDto.currentWeather),
            WeatherDataTimezone(weatherDto.timeZone)
            )
    }

    private fun doubleTemperaturesToString(temperatures: List<Double>): List<String> {
        return temperatures.map { "$it$degreeSign" } // maybe degree sign should be added somewhere else?
    }

    private fun mapHourly(hourlyFromAPI: HourlyWeatherDataDto): WeatherDataHourly {
        return WeatherDataHourly(
            temperatures = doubleTemperaturesToString(hourlyFromAPI.temperatures),
            weather_codes = hourlyFromAPI.weatherCodes
        )
    }

    private fun mapTodays(todayFromAPI: DailyWeatherDataDto): WeatherDataToday {
        return WeatherDataToday(
            max_temperature = "${todayFromAPI.todaysMaxTemperature[0]}$degreeSign",
            min_temperature = "${todayFromAPI.todaysMinTemperature[0]}$degreeSign"
        )
    }

    private fun mapCurrent(currentFromAPI: CurrentWeatherDataDto): WeatherDataCurrent {
        return WeatherDataCurrent(
            temperature = "${currentFromAPI.temperature}$degreeSign",
            weather_code = currentFromAPI.weatherCode,
            wind_speed = currentFromAPI.windSpeed
        )
    }

    private var degreeSign : String = "°"
}