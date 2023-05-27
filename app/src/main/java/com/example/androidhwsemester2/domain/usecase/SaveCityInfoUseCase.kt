package com.example.androidhwsemester2.domain.usecase

import com.example.androidhwsemester2.domain.entity.mapWeatherDataModel
import com.example.androidhwsemester2.domain.repository.WeatherRepository
import com.example.androidhwsemester2.presentation.model.WeatherDataModel
import javax.inject.Inject

class SaveCityInfoUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(model: WeatherDataModel):Long{
        return weatherRepository.saveCity(model.mapWeatherDataModel())
    }
}