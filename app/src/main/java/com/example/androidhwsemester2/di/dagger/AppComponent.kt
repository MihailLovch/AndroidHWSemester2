package com.example.androidhwsemester2.di.dagger

import android.content.Context
import com.example.androidhwsemester2.data.DataModule
import com.example.androidhwsemester2.presentation.fragments.AddingBottomSheetFragment
import com.example.androidhwsemester2.presentation.fragments.CityInfoFragment
import com.example.androidhwsemester2.presentation.fragments.WeatherPagerFragment
import com.example.androidhwsemester2.presentation.fragments.debug.DebugViewModel
import com.example.androidhwsemester2.presentation.viewmodel.DailyInfoViewModel
import com.example.androidhwsemester2.presentation.viewmodel.ViewPagerViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class,AppBindsModule::class,ServiceModule::class])
interface AppComponent {

    fun inject(fragment: WeatherPagerFragment)
    fun inject(fragment: AddingBottomSheetFragment)
    fun inject(fragment: CityInfoFragment)

    fun debugViewModel() : DebugViewModel.Factory
    fun viewPagerViewModel(): ViewPagerViewModel.Factory
    fun dailyInfoViewModel(): DailyInfoViewModel.Factory

    @Component.Builder
    interface Builder{
        fun context(@BindsInstance context: Context): Builder
        fun build(): AppComponent
    }
}