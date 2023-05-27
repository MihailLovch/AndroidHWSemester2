package com.example.androidhwsemester2.data.mappers

import com.example.androidhwsemester2.data.remote.model.WeatherMainInfo
import com.example.androidhwsemester2.domain.entity.WeatherDayInfo
import com.example.androidhwsemester2.domain.entity.WeatherEntity
import java.util.*

fun WeatherMainInfo?.mapToWeatherDayEntity(): WeatherDayInfo {
    return this?.let {
        WeatherDayInfo(
            temp = temp ?: 0.0f,
            feelsLike = feelsLike ?: 0.0f,
            tempMin = tempMin ?: 0.0f,
            tempMax = tempMax ?: 0.0f,
            pressure = pressure ?: 0.0f,
            humidity = humidity ?: 0.0f,
        )
    } ?: WeatherDayInfo(
        temp = 0.0f,
        feelsLike = 0.0f,
        tempMin = 0.0f,
        tempMax = 0.0f,
        pressure = 0.0f,
        humidity = 0.0f,
    )
}