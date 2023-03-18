package com.example.androidhwsemester2.domain.usecase

import com.example.androidhwsemester2.domain.repository.WeatherRepository
import com.example.androidhwsemester2.presentation.model.WeatherDataModel

class GetWeatherByCurrentCordsUseCase(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(lat: Double, lon: Double): WeatherDataModel {
        return (weatherRepository.checkCache(lat = lat, lon = lon)
            ?: weatherRepository.getWeatherInfoByCords(lat = lat, lon = lon)).mapWeatherEntity()
    }
}