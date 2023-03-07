package com.example.androidhwsemester2.data.local.maper

import com.example.androidhwsemester2.data.local.entity.CityWeatherInfo
import com.example.androidhwsemester2.data.remote.model.WeatherResponse
import java.util.Calendar

object ResponseMapper {
    fun toEntity(response: WeatherResponse?): CityWeatherInfo =
        CityWeatherInfo(
            cityName = response?.cityName ?: "",
            temperature = response?.main?.temp ?: 0f,
            humidity = response?.main?.humidity ?: 0f,
            pressure = response?.main?.pressure ?: 0f,
            windSpeed = response?.wind?.speed ?: 0f,
            iconId = response?.weatherList?.first()?.icon ?: "",
            lat = response?.coords?.latitude ?: 0.0,
            lon = response?.coords?.longitude ?: 0.0,
            lastSearch = Calendar.getInstance().time
        )
}