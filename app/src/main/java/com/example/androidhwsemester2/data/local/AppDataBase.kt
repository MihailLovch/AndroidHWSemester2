package com.example.androidhwsemester2.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.androidhwsemester2.data.local.dao.CityWeatherInfoDao
import com.example.androidhwsemester2.data.local.entity.CityWeatherInfo
import com.example.androidhwsemester2.data.local.typeconverter.DateConverter

@Database(
    entities = [CityWeatherInfo::class],
    version = 1
)
@TypeConverters(DateConverter::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun cityWeatherInfoDao(): CityWeatherInfoDao
}