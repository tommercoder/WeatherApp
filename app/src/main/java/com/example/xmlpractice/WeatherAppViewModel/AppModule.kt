package com.example.xmlpractice.WeatherAppViewModel

import android.app.Application
import android.content.Context
import com.example.xmlpractice.data.remote.WeatherAPI
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideWeatherApi(): WeatherAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.open-meteo.com/")
            .build()
            .create(WeatherAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideLocationProvider(application : Application) : FusedLocationProviderClient{
        return LocationServices.getFusedLocationProviderClient(application)
    }
}