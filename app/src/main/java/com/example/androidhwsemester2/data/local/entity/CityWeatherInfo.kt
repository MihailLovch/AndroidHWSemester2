package com.example.androidhwsemester2.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "cache",
    indices = [Index(value = ["city_name"], unique = true)]
)
data class CityWeatherInfo(
    @PrimaryKey
    @ColumnInfo(name = "city_name")
    val cityName: String,
    val temperature: Float,
    val humidity: Float,
    val pressure: Float,
    @ColumnInfo(name = "wind_speed")
    val windSpeed: Float,
    @ColumnInfo(name = "icon_id")
    val iconId: String,
    @ColumnInfo(name = "last_search")
    val lastSearch: Date,
    val lat:Double,
    val lon:Double,
    @ColumnInfo(name ="request_count")
    var requestCount: Int = 1
): java.io.Serializable
