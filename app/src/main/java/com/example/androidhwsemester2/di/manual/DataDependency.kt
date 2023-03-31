package com.example.androidhwsemester2.di.manual

import android.content.Context
import com.example.androidhwsemester2.data.local.repository.WeatherInfoRepository
import com.example.androidhwsemester2.data.remote.network.OpenWeatherApiService
import com.example.androidhwsemester2.data.remote.network.OpenWeatherService
import com.example.androidhwsemester2.data.repository.WeatherRepositoryImpl
import com.example.androidhwsemester2.domain.repository.WeatherRepository
import com.example.androidhwsemester2.domain.usecase.GetCitiesFromDataBaseUseCase
import com.example.androidhwsemester2.domain.usecase.GetWeatherByCityNameUseCase
import com.example.androidhwsemester2.domain.usecase.GetWeatherByCurrentCordsUseCase
import com.example.androidhwsemester2.domain.usecase.SaveCityInfoUseCase

class DataDependency(context: Context) {

    private val remoteSource: OpenWeatherApiService = OpenWeatherService.getInstance()
    private val localSource: WeatherInfoRepository = WeatherInfoRepository()

    private val weatherRepository: WeatherRepository = WeatherRepositoryImpl(
        remoteSource = remoteSource,
        localSource = localSource,
    )

    val getWeatherByNameUseCase: GetWeatherByCityNameUseCase = GetWeatherByCityNameUseCase(weatherRepository)
    val getWeatherByCordsUseCase: GetWeatherByCurrentCordsUseCase = GetWeatherByCurrentCordsUseCase(weatherRepository)
    val getCitiesFromDataBaseUseCase: GetCitiesFromDataBaseUseCase = GetCitiesFromDataBaseUseCase(weatherRepository)
    val saveCityInfoUseCase: SaveCityInfoUseCase = SaveCityInfoUseCase(weatherRepository)
}