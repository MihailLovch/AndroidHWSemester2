package com.example.androidhwsemester2.data.local.dao

import androidx.room.*
import com.example.androidhwsemester2.data.local.entity.CityWeatherInfo

@Dao
interface CityWeatherInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(item: CityWeatherInfo): Long

    @Query("SELECT * FROM cache WHERE city_name = :name LIMIT 1")
    suspend fun getInfoByCityName(name: String) : CityWeatherInfo?

    @Query("SELECT * FROM cache WHERE abs(lat - :lat)<0.1  AND abs(lon - :lon)<0.1  LIMIT 1")
    suspend fun getInfoByCityCords(lat: Double, lon: Double) : CityWeatherInfo?

    @Query("SELECT * from cache")
    suspend fun getAll(): List<CityWeatherInfo>
}