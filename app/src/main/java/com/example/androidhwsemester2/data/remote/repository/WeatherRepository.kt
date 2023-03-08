package com.example.androidhwsemester2.data.remote.repository

import com.example.androidhwsemester2.data.local.entity.CityWeatherInfo
import com.example.androidhwsemester2.data.remote.model.WeatherResponse
import com.example.androidhwsemester2.data.remote.network.OpenWeatherApiService
import com.example.androidhwsemester2.data.remote.network.OpenWeatherService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository {
    private var weatherService: OpenWeatherApiService? = null

    init {
        weatherService = OpenWeatherService.getInstance()
    }

    suspend fun saveWeatherInfo(info: CityWeatherInfo){

    }
    suspend fun getWeatherInfoByCityName(city: String): WeatherResponse? {
        return withContext(Dispatchers.IO) {
            weatherService?.getWeatherByCityName(city = city)
        }
    }

    suspend fun getWeatherInfoByCords(lat: Double, lon: Double): WeatherResponse? {
        return withContext(Dispatchers.IO) {
            weatherService?.getWeatherByCords(
                latitude = lat,
                longitude = lon
            )
        }
    }
}