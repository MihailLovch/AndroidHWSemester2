package com.example.androidhwsemester2.data.remote.network

import com.example.androidhwsemester2.data.remote.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApiService {
    @GET("weather")
    suspend fun getWeatherByCityName(
        @Query("q") city: String
    ): WeatherResponse

    @GET("weather")
    suspend fun getWeatherByCords(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
    ): WeatherResponse
}