package com.example.androidhwsemester2.domain.entity

import androidx.room.ColumnInfo
import com.example.androidhwsemester2.presentation.model.WeatherDataModel
import java.util.*

data class WeatherEntity(
    val cityName: String,
    val temperature: Float,
    val humidity: Float,
    val pressure: Float,
    val windSpeed: Float,
    val iconId: String,
    val lastSearch: Date,
    val lat: Double,
    val lon: Double,
) {
    fun mapWeatherEntity(): WeatherDataModel {
        return WeatherDataModel(
            cityName = this.cityName,
            temperature = this.temperature,
            humidity = this.humidity,
            pressure = this.pressure,
            windSpeed = this.windSpeed,
            iconId = this.iconId,
            lastSearch = this.lastSearch,
            lat = this.lat,
            lon = this.lon,
        )
    }
}
