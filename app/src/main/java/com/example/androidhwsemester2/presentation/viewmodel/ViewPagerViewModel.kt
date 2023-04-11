package com.example.androidhwsemester2.presentation.viewmodel

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.androidhwsemester2.di.manual.ViewModelArgsKey
import com.example.androidhwsemester2.domain.usecase.GetCitiesFromDataBaseUseCase
import com.example.androidhwsemester2.domain.usecase.GetWeatherByCityNameUseCase
import com.example.androidhwsemester2.domain.usecase.SaveCityInfoUseCase
import com.example.androidhwsemester2.presentation.extensions.handleException
import com.example.androidhwsemester2.presentation.model.WeatherDataModel
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

class ViewPagerViewModel @AssistedInject constructor(
    private val getCitesUseCase: GetCitiesFromDataBaseUseCase,
    private val getWeatherByCityNameUseCase: GetWeatherByCityNameUseCase,
    private val saveCityUseCase: SaveCityInfoUseCase
) : ViewModel() {

    @AssistedFactory
    interface Factory{
        fun create():ViewPagerViewModel
    }

    private val _weatherListState: ArrayList<MutableLiveData<WeatherDataModel?>> = arrayListOf()
    var weatherListState: List<LiveData<WeatherDataModel?>> = _weatherListState


    // ругалось на лайвдату с массивом лайвадат, эта лайвдата для того чтобы отслеживать изменение массива сверху
    private val _listState: MutableLiveData<Int> = MutableLiveData(null)
    var listState: LiveData<Int> = _listState

    private val _isRefreshingState: MutableLiveData<Boolean> = MutableLiveData(null)
    var isRefreshingState: LiveData<Boolean> = _isRefreshingState

    private val _errorState: MutableLiveData<String> = MutableLiveData("")
    var errorState: LiveData<String> = _errorState

    private val _firstDialogState: MutableLiveData<Boolean> = MutableLiveData(false)
    var firstDialogState: LiveData<Boolean> = _firstDialogState

    private val _elementAddedState: MutableLiveData<Int> = MutableLiveData(null)
    var elementAddedState: LiveData<Int> = _elementAddedState

    init {
        viewModelScope.launch {
            val result = getCitesUseCase()
            _weatherListState.addAll(result.map { MutableLiveData(it) })
            if (result.isNotEmpty()) {
                _listState.value = result.size
            } else {
                _firstDialogState.value = true
            }
        }

    }

    fun refreshData(position: Int) {
        viewModelScope.launch {
            _weatherListState[position].value =
                getWeatherByCityNameUseCase(_weatherListState[position].value?.cityName.toString())
            _isRefreshingState.value = false
        }
    }

    fun refreshData(){
        for (i in _weatherListState.indices){
            refreshData(i)
        }
    }

    fun addNewCityToList(name: String) {
        viewModelScope.launch {
            runCatching {
                getWeatherByCityNameUseCase(name)
            }.onSuccess { model ->
                var flag = false
                for ((index, value) in _weatherListState.withIndex()) {
                    if (value.value?.cityName == model.cityName) {
                        flag = true
                        _weatherListState[index].value = model
                    }
                }
                if (!flag) {
                    _weatherListState.add(MutableLiveData(model))
                    _listState.value = _weatherListState.size
                    _elementAddedState.value = _weatherListState.size
                }
                saveCityUseCase(model)
            }.onFailure { ex ->
                _errorState.value = ex.handleException()
            }
        }
    }

    fun getCurrentCities(): List<WeatherDataModel?> {
        return _weatherListState.map { it.value }
    }
}