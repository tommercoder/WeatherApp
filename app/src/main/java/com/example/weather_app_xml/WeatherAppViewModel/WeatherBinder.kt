package com.example.weather_app_xml.WeatherAppViewModel

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//add installin and descriptionasd
@Module
@InstallIn(SingletonComponent::class)
abstract class WeatherBinder {

    @Binds
    @Singleton
    abstract fun bindWeatherService(weatherService : WeatherService) : IWeatherService
}