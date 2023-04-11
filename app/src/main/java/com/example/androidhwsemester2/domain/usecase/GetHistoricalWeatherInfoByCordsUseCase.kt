package com.example.androidhwsemester2.domain.usecase

import com.example.androidhwsemester2.domain.entity.WeatherDayInfo
import com.example.androidhwsemester2.domain.repository.WeatherRepository
import javax.inject.Inject

class GetHistoricalWeatherInfoByCordsUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository,
) {
    suspend operator fun invoke(lat: Double, long: Double, count: Int): List<WeatherDayInfo> {
        return weatherRepository.getWeatherHistoricalInfoByCords(
            lat = lat,
            long = long,
            count = count
        )
    }
}