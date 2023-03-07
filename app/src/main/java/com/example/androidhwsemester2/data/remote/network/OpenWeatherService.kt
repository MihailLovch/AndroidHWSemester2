package com.example.androidhwsemester2.data.remote.network

import com.example.androidhwsemester2.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object OpenWeatherService {


    private val okHttpClient: OkHttpClient by lazy {
        createOkHttpClient()
    }
    private val retrofitInstance: OpenWeatherApiService by lazy {
        createRetrofitInstance()
    }

    private fun createOkHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val modifiedUrl = chain.request().url.newBuilder()
                    .addQueryParameter("appid", BuildConfig.WEATHER_KEY)
                    .addQueryParameter("units", "metric")
                    .build()

                val request = chain.request().newBuilder().url(modifiedUrl).build()
                chain.proceed(request)
            }
        if (BuildConfig.DEBUG) {
            client.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }

        return client.build()
    }

    private fun createRetrofitInstance(): OpenWeatherApiService {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(BuildConfig.WEATHER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofitBuilder.create(OpenWeatherApiService::class.java)
    }


    fun getInstance()  : OpenWeatherApiService {
        return retrofitInstance
    }
}