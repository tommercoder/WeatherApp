package com.example.weather_app_xml.WeatherAppViewModel

import androidx.annotation.DrawableRes
import com.example.weather_app_xml.R

sealed class WeatherType( //what's sealed?
    val weatherDesc: String,
    @DrawableRes val iconRes: Int,
    @DrawableRes val fullscreenImage: Int
) {
    object ClearSky : WeatherType( // why object?
        weatherDesc = "Clear sky",
        iconRes = R.drawable.clear_sky,
        fullscreenImage = R.drawable.clear_sky_fullscreen
    )
    object MainlyClear : WeatherType(
        weatherDesc = "Mainly clear",
        iconRes = R.drawable.mainly_clear,
        fullscreenImage = R.drawable.mainly_clear_fullscreen
    )
    object PartlyCloudy : WeatherType(
        weatherDesc = "Partly cloudy",
        iconRes = R.drawable.partly_cloudy,
        fullscreenImage = R.drawable.partly_cloudy_fullscreen
    )
    object Overcast : WeatherType(
        weatherDesc = "Overcast",
        iconRes = R.drawable.overcast,
        fullscreenImage = R.drawable.overcast_fullscreen
    )
    object Rainy : WeatherType(
        weatherDesc = "Rainy",
        iconRes = R.drawable.rainy,
        fullscreenImage = R.drawable.overcast_fullscreen
    )
    object Drizzle : WeatherType(
        weatherDesc = "Drizzle",
        iconRes = R.drawable.drizzly,
        fullscreenImage = R.drawable.overcast_fullscreen
    )

    companion object {
        fun fromWMO(code: Int): WeatherType {
            return when(code) {
                0 -> ClearSky
                1 -> MainlyClear
                2 -> PartlyCloudy
                3 -> Overcast
                61, 63, 65 -> Rainy
                51, 53, 55 -> Drizzle
                else -> ClearSky
            }
        }
    }
}
