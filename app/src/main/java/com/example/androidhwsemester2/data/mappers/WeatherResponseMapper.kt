package com.example.androidhwsemester2.data.mappers

import com.example.androidhwsemester2.data.local.entity.CityWeatherInfo
import com.example.androidhwsemester2.data.remote.model.WeatherResponse
import com.example.androidhwsemester2.domain.entity.WeatherEntity
import java.util.Calendar

object WeatherResponseMapper {
    fun mapToDB(response: WeatherResponse?): CityWeatherInfo {
        return response?.let {
            CityWeatherInfo(
                cityName = response.cityName ?: "",
                temperature = response.main?.temp ?: 0f,
                humidity = response.main?.humidity ?: 0f,
                pressure = response.main?.pressure ?: 0f,
                windSpeed = response.wind?.speed ?: 0f,
                iconId = response.weatherList?.first()?.icon ?: "",
                lat = response.coords?.latitude ?: 0.0,
                lon = response.coords?.longitude ?: 0.0,
                lastSearch = Calendar.getInstance().time
            )
        } ?: CityWeatherInfo(
            cityName = "",
            temperature = 0f,
            humidity = 0f,
            pressure = 0f,
            windSpeed = 0f,
            iconId = "",
            lat = 0.0,
            lon = 0.0,
            lastSearch = Calendar.getInstance().time
        )
    }

    fun mapToDomain(response: WeatherResponse?): WeatherEntity {
        return response?.let {
            WeatherEntity(
                cityName = response.cityName ?: "",
                temperature = response.main?.temp ?: 0f,
                humidity = response.main?.humidity ?: 0f,
                pressure = response.main?.pressure ?: 0f,
                windSpeed = response.wind?.speed ?: 0f,
//                iconId = response.weatherList?.first()?.icon ?: "",
                iconId = if  (response.weatherList?.isEmpty() ?: true) "" else response.weatherList?.first()?.icon ?: "",
                lat = response.coords?.latitude ?: 0.0,
                lon = response.coords?.longitude ?: 0.0,
                lastSearch = Calendar.getInstance().time
            )
        } ?: WeatherEntity(
            cityName = "",
            temperature = 0f,
            humidity = 0f,
            pressure = 0f,
            windSpeed = 0f,
            iconId = "",
            lat = 0.0,
            lon = 0.0,
            lastSearch = Calendar.getInstance().time
        )
    }
}