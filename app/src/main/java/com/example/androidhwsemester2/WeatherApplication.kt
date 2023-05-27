package com.example.androidhwsemester2

import android.app.Application
import com.example.androidhwsemester2.di.dagger.AppComponent
import com.example.androidhwsemester2.di.dagger.DaggerAppComponent

class WeatherApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .context(this)
            .build()
    }
}