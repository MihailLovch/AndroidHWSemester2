package com.example.androidhwsemester2.domain.usecase

import com.example.androidhwsemester2.domain.entity.mapWeatherEntity
import com.example.androidhwsemester2.domain.repository.WeatherRepository
import com.example.androidhwsemester2.presentation.model.WeatherDataModel
import javax.inject.Inject

class GetWeatherByCurrentCordsUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(lat: Double, lon: Double, cache: Boolean = false): WeatherDataModel {
        return weatherRepository.getWeatherInfoByCords(lat = lat, lon = lon, cache).mapWeatherEntity()
    }
}