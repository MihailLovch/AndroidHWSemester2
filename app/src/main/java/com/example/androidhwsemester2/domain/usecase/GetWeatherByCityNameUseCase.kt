package com.example.androidhwsemester2.domain.usecase

import com.example.androidhwsemester2.data.mappers.WeatherEntityMapper
import com.example.androidhwsemester2.domain.entity.WeatherEntity
import com.example.androidhwsemester2.domain.entity.mapWeatherEntity
import com.example.androidhwsemester2.domain.repository.WeatherRepository
import com.example.androidhwsemester2.presentation.model.WeatherDataModel

class GetWeatherByCityNameUseCase(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(cityName: String, cache: Boolean = false): WeatherDataModel {
        return weatherRepository.getWeatherInfoByCityName(cityName, cache).mapWeatherEntity()

    }
}