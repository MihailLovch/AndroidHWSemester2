package com.example.androidhwsemester2.di

import android.content.Context
import com.example.androidhwsemester2.data.local.repository.WeatherInfoRepository
import com.example.androidhwsemester2.data.remote.network.OpenWeatherApiService
import com.example.androidhwsemester2.data.remote.network.OpenWeatherService
import com.example.androidhwsemester2.data.repository.WeatherRepositoryImpl
import com.example.androidhwsemester2.domain.repository.WeatherRepository
import com.example.androidhwsemester2.domain.usecase.GetWeatherByCityNameUseCase
import com.example.androidhwsemester2.domain.usecase.GetWeatherByCurrentCordsUseCase

class DataDependency(context: Context) {

    private val remoteSource: OpenWeatherApiService = OpenWeatherService.getInstance()
    private val localSource: WeatherInfoRepository = WeatherInfoRepository(context)

    private val weatherRepository: WeatherRepository = WeatherRepositoryImpl(
        remoteSource = remoteSource,
        localSource = localSource,
    )

    val getWeatherByNameUseCase: GetWeatherByCityNameUseCase = GetWeatherByCityNameUseCase(weatherRepository)
    val getWeatherByCordsUseCase: GetWeatherByCurrentCordsUseCase = GetWeatherByCurrentCordsUseCase(weatherRepository)

}