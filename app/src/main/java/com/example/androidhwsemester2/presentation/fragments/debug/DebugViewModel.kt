package com.example.androidhwsemester2.presentation.fragments.debug

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidhwsemester2.domain.usecase.GetCitiesAndRequestCountUseCase
import com.example.androidhwsemester2.domain.usecase.GetLastRequestTimeUseCase
import com.example.androidhwsemester2.domain.usecase.GetUserTokenUseCase
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.Date


class DebugViewModel @AssistedInject constructor(
    private val getLastRequestTimeUseCase: GetLastRequestTimeUseCase,
    private val getCitiesAndRequestCountUseCase: GetCitiesAndRequestCountUseCase,
    private val getUserTokenUseCase: GetUserTokenUseCase,
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val _requestCountState: MutableLiveData<List<Pair<String, Int>>> = MutableLiveData()
    val requestCountState: LiveData<List<Pair<String, Int>>> = _requestCountState

    private val _lastTimeRequestState: MutableLiveData<Date> = MutableLiveData()
    val lastTimeRequestState: LiveData<Date> = _lastTimeRequestState

    private var token: String? = null

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
                    _requestCountState.value = list
                },
            getUserTokenUseCase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ newToken ->
                    token = newToken
                }){
                  it -> token = it.localizedMessage
                },
        )
    }

    fun deleteToken() {
        FirebaseMessaging.getInstance().deleteToken()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("TEST-TAG", "Token deleted")
                } else {
                    Log.w("TEST-TAG", "Failed to delete token")
                }
            }
    }
    fun getToken(): String? {
        return token
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