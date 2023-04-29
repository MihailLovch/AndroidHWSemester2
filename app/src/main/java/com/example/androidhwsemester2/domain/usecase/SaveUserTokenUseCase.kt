package com.example.androidhwsemester2.domain.usecase

import com.example.androidhwsemester2.domain.repository.WeatherRepository
import javax.inject.Inject

class SaveUserTokenUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    operator fun invoke(token: String) =
        weatherRepository.saveToken(token)
}