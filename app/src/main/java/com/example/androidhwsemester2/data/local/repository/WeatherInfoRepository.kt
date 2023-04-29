package com.example.androidhwsemester2.data.local.repository

import android.content.Context
import androidx.room.Room
import com.example.androidhwsemester2.data.local.AppDataBase
import com.example.androidhwsemester2.data.local.dao.CityWeatherInfoDao
import com.example.androidhwsemester2.data.local.entity.CityWeatherInfo
import io.reactivex.rxjava3.core.Single
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
        val count = weatherInfoDao.getInfoByCityName(info.cityName)?.requestCount ?: 0
        return weatherInfoDao.save(info.apply { requestCount = count+1 })
    }
    suspend fun getAllCities(): List<CityWeatherInfo?>{
        return weatherInfoDao.getAll()
    }

    fun getLastRequestDate(): Single<Long> = weatherInfoDao.getLastRequestDate()

    fun getAllCitiesRX(): Single<List<CityWeatherInfo>> =
        weatherInfoDao.getAllCities()
}