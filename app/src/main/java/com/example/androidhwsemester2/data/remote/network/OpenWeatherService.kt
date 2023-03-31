package com.example.androidhwsemester2.data.remote.network

import com.example.androidhwsemester2.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

object OpenWeatherService {
    private lateinit var retrofitInstance: OpenWeatherApiService
    fun getInstance()  : OpenWeatherApiService {
        return retrofitInstance
    }
}