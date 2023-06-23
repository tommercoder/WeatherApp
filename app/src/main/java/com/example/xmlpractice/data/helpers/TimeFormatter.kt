package com.example.xmlpractice.data.helpers

import com.google.android.datatransport.runtime.dagger.Module
import com.google.android.datatransport.runtime.dagger.Provides
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Module
object TimeFormatter {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
    @Provides
    fun getFormattedTimeForHourlyCard(to_format: String) : String {
        return LocalDateTime.parse(to_format, formatter).toLocalTime().toString()
    }

    @Provides
    fun getHour(time: String) : Int {
        //return times.map {  }
        return LocalDateTime.parse(time, formatter).hour
    }
}