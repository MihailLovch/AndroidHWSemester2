package com.example.androidhwsemester2.domain.entity

import com.google.gson.annotations.SerializedName

data class WeatherDayInfo(
    val temp: Float,
    val feelsLike: Float,
    val tempMin: Float,
    val tempMax: Float,
    val pressure: Float,
    val humidity: Float?
)