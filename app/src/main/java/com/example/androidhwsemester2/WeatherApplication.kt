package com.example.androidhwsemester2

import android.app.Application
import com.example.androidhwsemester2.di.dagger.AppComponent
import com.example.androidhwsemester2.di.dagger.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class WeatherApplication : Application(), HasAndroidInjector{

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .context(this)
            .build()
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}