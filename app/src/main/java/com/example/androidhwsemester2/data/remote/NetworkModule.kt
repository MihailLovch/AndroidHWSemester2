package com.example.androidhwsemester2.data.remote

import com.example.androidhwsemester2.BuildConfig
import com.example.androidhwsemester2.data.remote.network.OpenWeatherApiService
import com.example.androidhwsemester2.presentation.viewmodel.ViewPagerViewModel
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(
        appIdInterceptor: Interceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient{
        val client = OkHttpClient.Builder()
            .addInterceptor(appIdInterceptor)
        if (BuildConfig.DEBUG) {
            client.addInterceptor(httpLoggingInterceptor)
        }
        return client.build()

    }
    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): OpenWeatherApiService =
        Retrofit.Builder()
            .baseUrl(BuildConfig.WEATHER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenWeatherApiService::class.java)
    @Singleton
    @Provides
    fun provideAppIdInterceptor(): Interceptor{
        return Interceptor { chain ->
            val modifiedUrl = chain.request().url.newBuilder()
                .addQueryParameter("appid", BuildConfig.WEATHER_KEY)
                .addQueryParameter("units", "metric")
                .build()

            val request = chain.request().newBuilder().url(modifiedUrl).build()
            chain.proceed(request)
        }
    }
    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

}