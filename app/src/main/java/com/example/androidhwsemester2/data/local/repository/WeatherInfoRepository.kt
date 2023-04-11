package com.example.androidhwsemester2.data.local.repository

import android.content.Context
import androidx.room.Room
import com.example.androidhwsemester2.data.local.AppDataBase
import com.example.androidhwsemester2.data.local.dao.CityWeatherInfoDao
import com.example.androidhwsemester2.data.local.entity.CityWeatherInfo
import javax.inject.Inject

class WeatherInfoRepository @Inject constructor() {
    @Inject
    lateinit var weatherInfoDao: CityWeatherInfoDao

    suspend fun getCityWeatherInfoByName(name: String): CityWeatherInfo? {
        return weatherInfoDao.getInfoByCityName(name = name)
    }

    suspend fun getCityWeatherInfoByCords(lon: Double, lat: Double): CityWeatherInfo? {
        return weatherInfoDao.getInfoByCityCords(lon = lon, lat = lat)
    }
    suspend fun saveCityWeatherInfo(info: CityWeatherInfo): Long{
        return weatherInfoDao.save(info)
    }
    suspend fun getAllCities(): List<CityWeatherInfo?>{
        return weatherInfoDao.getAll()
    }
}