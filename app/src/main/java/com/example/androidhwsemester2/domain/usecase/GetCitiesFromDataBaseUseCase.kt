package com.example.androidhwsemester2.domain.usecase

import com.example.androidhwsemester2.domain.entity.mapWeatherEntity
import com.example.androidhwsemester2.domain.repository.WeatherRepository
import com.example.androidhwsemester2.presentation.model.WeatherDataModel

class GetCitiesFromDataBaseUseCase(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(): List<WeatherDataModel> {
        return weatherRepository.getAllCities().map {it.mapWeatherEntity() }
    }
}