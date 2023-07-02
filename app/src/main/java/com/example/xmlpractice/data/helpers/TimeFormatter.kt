package com.example.xmlpractice.data.helpers

import com.example.xmlpractice.WeatherAppViewModel.Constants
import com.google.android.datatransport.runtime.dagger.Module
import com.google.android.datatransport.runtime.dagger.Provides
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Module
object TimeFormatter {
    private val formatter = DateTimeFormatter.ofPattern(Constants.time_format)
    @Provides
    fun getFormattedTimeForHourlyCard(to_format: String) : String {
        return LocalDateTime.parse(to_format, formatter).toLocalTime().toString()
    }
}