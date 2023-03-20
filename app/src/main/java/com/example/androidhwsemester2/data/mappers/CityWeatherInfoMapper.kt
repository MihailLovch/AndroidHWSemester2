package com.example.androidhwsemester2.data.mappers

import com.example.androidhwsemester2.data.local.entity.CityWeatherInfo
import com.example.androidhwsemester2.domain.entity.WeatherEntity
import java.util.Calendar

object CityWeatherInfoMapper {

    fun map(item: CityWeatherInfo?): WeatherEntity {
        return item?.let {
            WeatherEntity(
                cityName = item.cityName,
                temperature = item.temperature,
                humidity = item.humidity,
                pressure = item.pressure,
                windSpeed = item.windSpeed,
                iconId = item.iconId,
                lastSearch = item.lastSearch,
                lat = item.lat,
                lon = item.lon
            )
        } ?: WeatherEntity(
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