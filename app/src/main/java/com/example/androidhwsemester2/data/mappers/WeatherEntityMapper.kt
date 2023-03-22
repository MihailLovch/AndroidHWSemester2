package com.example.androidhwsemester2.data.mappers

import com.example.androidhwsemester2.data.local.entity.CityWeatherInfo
import com.example.androidhwsemester2.domain.entity.WeatherEntity
import java.util.*

object WeatherEntityMapper {

    fun map(item: WeatherEntity?): CityWeatherInfo {
        return item?.let {
            with(item) {
                CityWeatherInfo(
                    cityName = cityName,
                    temperature = temperature,
                    humidity = humidity,
                    pressure = pressure,
                    windSpeed = windSpeed,
                    iconId = iconId,
                    lastSearch = lastSearch,
                    lat = lat,
                    lon = lon,
                )
            }
        } ?: CityWeatherInfo(
            cityName = "",
            temperature = 0f,
            humidity = 0f,
            pressure = 0f,
            windSpeed = 0f,
            iconId = "",
            lastSearch = Calendar.getInstance().time,
            lat = 0.0,
            lon = 0.0
        )
    }

}