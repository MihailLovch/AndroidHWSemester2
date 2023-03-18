package com.example.androidhwsemester2.di

import androidx.lifecycle.viewmodel.CreationExtras
import com.example.androidhwsemester2.domain.usecase.GetWeatherByCityNameUseCase
import com.example.androidhwsemester2.domain.usecase.GetWeatherByCurrentCordsUseCase

object ViewModelArgsKey {

    val getWeatherByNameUseCase = object : CreationExtras.Key<GetWeatherByCityNameUseCase> {}
    val getWeatherByCordsUseCase = object : CreationExtras.Key<GetWeatherByCurrentCordsUseCase> {}

}