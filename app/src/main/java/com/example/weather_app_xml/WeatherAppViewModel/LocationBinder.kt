package com.example.weather_app_xml.WeatherAppViewModel

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton


//add installin and description
@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
abstract class LocationBinder {

    @Binds
    @Singleton
    abstract fun bindLocationService(locationRepository: LocationRepository) : ILocationRepository
}