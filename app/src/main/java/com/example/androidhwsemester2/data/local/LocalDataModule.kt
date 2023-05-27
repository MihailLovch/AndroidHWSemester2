package com.example.androidhwsemester2.data.local

import android.content.Context
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.room.Room
import com.example.androidhwsemester2.data.local.dao.CityWeatherInfoDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object LocalDataModule {

    @Provides
    @Singleton
    fun provideAppDataBase(
        context: Context,
    ): AppDataBase = Room.databaseBuilder(
        context,
        AppDataBase::class.java,
        "itis.db.lovc",
    ).build()


    @Provides
    @Singleton
    fun provideCityWeatherInfoDao(
        dataBase: AppDataBase,
    ): CityWeatherInfoDao = dataBase.cityWeatherInfoDao()

}