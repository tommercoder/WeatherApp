package com.example.weather_app_xml.WeatherAppViewModel


fun <T> checkAndReturnSublist(start: Int, end: Int, list: List<T>): List<T> {
    if (end <= list.size) {
        return list.subList(start, end)
    }

    return emptyList()
}