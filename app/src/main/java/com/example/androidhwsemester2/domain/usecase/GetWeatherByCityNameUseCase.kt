package com.example.androidhwsemester2.domain.usecase

import com.example.androidhwsemester2.domain.repository.WeatherRepository
import com.example.androidhwsemester2.presentation.model.WeatherDataModel

class GetWeatherByCityNameUseCase(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(cityName: String): WeatherDataModel {
        return (weatherRepository.checkCache(cityName)
            ?: weatherRepository.getWeatherInfoByCityName(cityName)).mapWeatherEntity()
    }
}