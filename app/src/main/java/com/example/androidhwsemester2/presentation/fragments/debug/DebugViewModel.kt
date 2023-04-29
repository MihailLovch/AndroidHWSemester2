package com.example.androidhwsemester2.presentation.fragments.debug

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.androidhwsemester2.domain.usecase.GetCitiesAndRequestCountUseCase
import com.example.androidhwsemester2.domain.usecase.GetLastRequestTimeUseCase
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.Date

class DebugViewModel @AssistedInject constructor(
    private val getLastRequestTimeUseCase: GetLastRequestTimeUseCase,
    private val getCitiesAndRequestCountUseCase: GetCitiesAndRequestCountUseCase
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val _requestCountState: MutableLiveData<List<Pair<String, Int>>> = MutableLiveData()
    val requestCountState: LiveData<List<Pair<String, Int>>> = _requestCountState

    private val _lastTimeRequestState: MutableLiveData<Date> = MutableLiveData()
    val lastTimeRequestState: LiveData<Date> = _lastTimeRequestState

    init {
        disposables.addAll(
            getLastRequestTimeUseCase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { long ->
                    _lastTimeRequestState.value = Date(long)
                },
            getCitiesAndRequestCountUseCase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { list ->
                    _requestCountState.value  = list
                }

        )
    }

    @AssistedFactory
    interface Factory {
        fun create(): DebugViewModel
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}