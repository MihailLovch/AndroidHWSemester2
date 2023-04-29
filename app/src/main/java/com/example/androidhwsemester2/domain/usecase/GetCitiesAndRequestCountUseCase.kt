package com.example.androidhwsemester2.domain.usecase

import com.example.androidhwsemester2.domain.repository.WeatherRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetCitiesAndRequestCountUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository,
) {
    operator fun invoke(): Single<List<Pair<String, Int>>> =
        weatherRepository.getAllCitiesRX()
            .map { list -> list.map { Pair(it.cityName, it.requestCount) } }
}