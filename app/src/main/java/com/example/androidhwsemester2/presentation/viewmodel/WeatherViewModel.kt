package com.example.androidhwsemester2.presentation.viewmodel

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.androidhwsemester2.di.ViewModelArgsKey
import com.example.androidhwsemester2.domain.usecase.GetWeatherByCityNameUseCase
import com.example.androidhwsemester2.domain.usecase.GetWeatherByCurrentCordsUseCase
import com.example.androidhwsemester2.presentation.extensions.handleException
import com.example.androidhwsemester2.presentation.model.WeatherDataModel
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val getWeatherByName: GetWeatherByCityNameUseCase,
    private val getWeatherByCords: GetWeatherByCurrentCordsUseCase,
) : ViewModel() {

    private val _progressBarState: MutableLiveData<Boolean> = MutableLiveData(false)
    var progressBarState: LiveData<Boolean> = _progressBarState

    private val _weatherInfoState: MutableLiveData<WeatherDataModel?> = MutableLiveData(null)
    var weatherInfoState: LiveData<WeatherDataModel?> = _weatherInfoState

    private val _errorState: MutableLiveData<String> = MutableLiveData("")
    var errorState: LiveData<String> = _errorState

    fun requestCityByName(name: String) {
        viewModelScope.launch {
            _progressBarState.value = true
            runCatching {
                getWeatherByName(name,true)
            }.onSuccess { weatherDataModel ->
                _weatherInfoState.value = weatherDataModel
            }.onFailure { ex ->
                _errorState.value = ex.handleException()
            }
            _progressBarState.value = false
        }
    }

    fun requestCityBoCords(lat: Double, lon: Double) {
        viewModelScope.launch {
            _progressBarState.value = true
            runCatching {
                getWeatherByCords(lat = lat, lon = lon, true)
            }.onSuccess { weatherDataModel ->
                _weatherInfoState.value = weatherDataModel
            }.onFailure { ex ->
                _errorState.value = ex.handleException()
            }
            _progressBarState.value = false
        }
    }


    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val getWeatherByName = this[ViewModelArgsKey.getWeatherByNameUseCase]
                    ?: throw IllegalArgumentException()
                val getWeatherByCords = this[ViewModelArgsKey.getWeatherByCordsUseCase]
                    ?: throw IllegalArgumentException()
                WeatherViewModel(
                    getWeatherByName,
                    getWeatherByCords
                )
            }
        }
    }
}