package com.example.androidhwsemester2.data.local.typeconverter

import androidx.room.TypeConverter
import java.util.Date

object DateConverter {
    @TypeConverter
    fun toDate(time: Long): Date = Date(time)

    @TypeConverter
    fun fromDate(date: Date): Long = date.time
}