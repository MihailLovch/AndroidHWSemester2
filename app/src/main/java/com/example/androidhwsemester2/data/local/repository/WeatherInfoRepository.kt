package com.example.androidhwsemester2.data.local.repository

import android.content.Context
import androidx.room.Room
import com.example.androidhwsemester2.data.local.AppDataBase
import com.example.androidhwsemester2.data.local.entity.CityWeatherInfo

class WeatherInfoRepository(context: Context) {
    private val db by lazy {
        Room.databaseBuilder(context, AppDataBase::class.java, DATABASE_NAME).build()
    }
    private val weatherInfoDao by lazy {
        db.cityWeatherInfoDao()
    }

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
    companion object {
        private const val DATABASE_NAME = "itis.db.lovc"
    }
}