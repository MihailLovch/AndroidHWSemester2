package com.example.androidhwsemester2.di.dagger

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.androidhwsemester2.WeatherApplication

fun Context.appComponent(): AppComponent {
    return when (this) {
        is WeatherApplication -> appComponent
        else -> this.applicationContext.appComponent()
    }
}

inline fun <reified T : ViewModel> Fragment.lazyViewModel(
    activity: Boolean = false,
    noinline create: (stateHandle: SavedStateHandle) -> T
) = if (activity) {
    activityViewModels<T> {
        VMFactory(this, create)
    }
} else {
    viewModels<T> {
        VMFactory(this, create)
    }
}