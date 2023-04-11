package com.example.androidhwsemester2.di.dagger

import androidx.lifecycle.ViewModel
import com.example.androidhwsemester2.data.repository.WeatherRepositoryImpl
import com.example.androidhwsemester2.domain.repository.WeatherRepository
import com.example.androidhwsemester2.presentation.viewmodel.ViewPagerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module
interface AppBindsModule {
    @Binds
    fun bindWeatherRepositoryImpl_to_WeatherRepository(weatherRepository: WeatherRepositoryImpl): WeatherRepository

}