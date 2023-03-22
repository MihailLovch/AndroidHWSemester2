package com.example.androidhwsemester2.domain.usecase

import com.example.androidhwsemester2.domain.entity.mapWeatherDataModel
import com.example.androidhwsemester2.domain.repository.WeatherRepository
import com.example.androidhwsemester2.presentation.model.WeatherDataModel

class SaveCityInfoUseCase(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(model: WeatherDataModel):Long{
        return weatherRepository.saveCity(model.mapWeatherDataModel())
    }
}