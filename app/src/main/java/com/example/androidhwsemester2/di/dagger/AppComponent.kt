package com.example.androidhwsemester2.di.dagger

import android.content.Context
import com.example.androidhwsemester2.data.DataModule
import com.example.androidhwsemester2.presentation.fragments.AddingBottomSheetFragment
import com.example.androidhwsemester2.presentation.fragments.CityInfoFragment
import com.example.androidhwsemester2.presentation.fragments.WeatherPagerFragment
import com.example.androidhwsemester2.presentation.viewmodel.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class,AppBindsModule::class,ViewModelModule::class])
interface AppComponent {

    fun inject(fragment: WeatherPagerFragment)
    fun inject(fragment: AddingBottomSheetFragment)
    fun inject(fragment: CityInfoFragment)

    @Component.Builder
    interface Builder{
        fun context(@BindsInstance context: Context): Builder
        fun build(): AppComponent
    }
}