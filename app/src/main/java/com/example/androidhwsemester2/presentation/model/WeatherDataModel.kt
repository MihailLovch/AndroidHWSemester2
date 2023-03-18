package com.example.androidhwsemester2.presentation.model

import java.util.*

data class WeatherDataModel(
    val cityName: String,
    val temperature: Float,
    val humidity: Float,
    val pressure: Float,
    val windSpeed: Float,
    val iconId: String,
    val lastSearch: Date,
    val lat: Double,
    val lon: Double,
): java.io.Serializable
