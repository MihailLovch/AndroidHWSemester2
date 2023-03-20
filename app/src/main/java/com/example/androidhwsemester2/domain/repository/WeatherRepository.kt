package com.example.androidhwsemester2.domain.repository

import com.example.androidhwsemester2.data.local.entity.CityWeatherInfo
import com.example.androidhwsemester2.domain.entity.WeatherEntity

interface WeatherRepository {
    suspend fun getWeatherInfoByCityName(city: String, cache:Boolean): WeatherEntity
    suspend fun getWeatherInfoByCords(lat: Double, lon: Double, cache:Boolean): WeatherEntity
    suspend fun checkCache(lat: Double, lon: Double): WeatherEntity?
    suspend fun checkCache(cityName: String): WeatherEntity?

}