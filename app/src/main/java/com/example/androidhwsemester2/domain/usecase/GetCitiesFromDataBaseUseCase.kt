package com.example.androidhwsemester2.domain.usecase

import com.example.androidhwsemester2.domain.entity.mapWeatherEntity
import com.example.androidhwsemester2.domain.repository.WeatherRepository
import com.example.androidhwsemester2.presentation.model.WeatherDataModel
import javax.inject.Inject

class GetCitiesFromDataBaseUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(): List<WeatherDataModel> {
        return weatherRepository.getAllCities().map {it.mapWeatherEntity() }
    }
}