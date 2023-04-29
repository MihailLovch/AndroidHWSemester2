package com.example.androidhwsemester2.domain.repository

import com.example.androidhwsemester2.data.local.entity.CityWeatherInfo
import com.example.androidhwsemester2.domain.entity.WeatherDayInfo
import com.example.androidhwsemester2.domain.entity.WeatherEntity
import io.reactivex.rxjava3.core.Single

interface WeatherRepository {
    suspend fun getWeatherInfoByCityName(city: String, cache:Boolean): WeatherEntity
    suspend fun getWeatherInfoByCords(lat: Double, lon: Double, cache:Boolean): WeatherEntity
    suspend fun checkCache(lat: Double, lon: Double): WeatherEntity?
    suspend fun checkCache(cityName: String): WeatherEntity?

    suspend fun getAllCities(): List<WeatherEntity>

    suspend fun saveCity(model : WeatherEntity): Long
    suspend fun getWeatherHistoricalInfoByCords(
        lat: Double,
        long: Double,
        count: Int
    ): List<WeatherDayInfo>

    fun getLastRequestDate(): Single<Long>
    fun getAllCitiesRX(): Single<List<CityWeatherInfo>>
}