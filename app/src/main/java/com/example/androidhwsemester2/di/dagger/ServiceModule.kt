package com.example.androidhwsemester2.di.dagger

import com.example.androidhwsemester2.WeatherMessagingService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceModule {
    @ContributesAndroidInjector
    abstract fun contributeMyFirebaseMessagingService(): WeatherMessagingService
}