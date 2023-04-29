package com.example.androidhwsemester2.domain.usecase

import com.example.androidhwsemester2.domain.repository.WeatherRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetUserTokenUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {

    operator fun invoke(): Single<String> =
        weatherRepository.getToken()
}