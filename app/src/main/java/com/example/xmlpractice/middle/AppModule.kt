package com.example.xmlpractice.middle

import com.example.xmlpractice.data.remote.WeatherAPI
import com.google.android.datatransport.runtime.dagger.Module
import com.google.android.datatransport.runtime.dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
//@InstallIn(SingletonComponent::class) //deal with singleton somehow
object AppModule {

    @Provides
    fun getWeatherApi(): WeatherAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.open-meteo.com/")
            .build()
            .create(WeatherAPI::class.java)
    }
}