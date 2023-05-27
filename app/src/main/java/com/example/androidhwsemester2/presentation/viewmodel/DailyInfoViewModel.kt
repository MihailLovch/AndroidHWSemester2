package com.example.androidhwsemester2.presentation.viewmodel

import androidx.lifecycle.*
import com.example.androidhwsemester2.domain.entity.WeatherDayInfo
import com.example.androidhwsemester2.domain.usecase.GetHistoricalWeatherInfoByCordsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class DailyInfoViewModel @AssistedInject constructor(
    @Assisted(ASSISTED_LAT_KEY) private val lat: Double,
    @Assisted(ASSISTED_LONG_KEY) private val long: Double,
    private val getHistoricalInfoUseCase: GetHistoricalWeatherInfoByCordsUseCase
) : ViewModel() {

    var listWeatherState: LiveData<List<WeatherDayInfo>?> = liveData{
        emit(getHistoricalInfoUseCase(
            lat = lat,
            long = long,
            days = DAYS_NUMBER
        ))
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted(ASSISTED_LAT_KEY) lat: Double,
            @Assisted(ASSISTED_LONG_KEY) long: Double
        ): DailyInfoViewModel
    }

    companion object {
        private const val ASSISTED_LAT_KEY = "ASSISTED_LAT_KEY"
        private const val ASSISTED_LONG_KEY = "ASSISTED_LONG_KEY"
        private const val DAYS_NUMBER = 5
    }
}