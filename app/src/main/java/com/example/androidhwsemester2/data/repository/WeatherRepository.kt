package com.example.androidhwsemester2.data.repository

import com.example.androidhwsemester2.data.model.WeatherResponse
import com.example.androidhwsemester2.data.network.OpenWeatherApiService
import com.example.androidhwsemester2.data.network.OpenWeatherService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository {
    private var weatherService: OpenWeatherApiService? = null

    init {
        weatherService = OpenWeatherService.getInstance()
    }

    suspend fun getWeatherInfoByCityName(city: String): WeatherResponse? {
        Result
        return withContext(Dispatchers.IO) {
            weatherService?.getWeatherByCityName(city = city)
        }
    }

    suspend fun getWeatherInfoByCords(lat: Double, lon: Double): WeatherResponse? {
        Result
        return withContext(Dispatchers.IO) {
            weatherService?.getWeatherByCords(
                latitude = lat,
                longitude = lon
            )
        }
    }
}