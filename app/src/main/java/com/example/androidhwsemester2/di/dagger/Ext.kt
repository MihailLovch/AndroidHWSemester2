package com.example.androidhwsemester2.di.dagger

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.androidhwsemester2.WeatherApplication

fun Context.appComponent(): AppComponent{
    return when(this){
        is WeatherApplication -> appComponent
        else -> this.applicationContext.appComponent()
    }
}

inline fun <reified T : ViewModel> Fragment.lazyViewModel(
    noinline create: (stateHandle: SavedStateHandle) -> T
) = viewModels<T> {
    VMFactory(this, create)
}