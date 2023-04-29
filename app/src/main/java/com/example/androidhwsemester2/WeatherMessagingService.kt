package com.example.androidhwsemester2

import android.util.Log
import com.example.androidhwsemester2.domain.usecase.SaveUserTokenUseCase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.android.AndroidInjection
import javax.inject.Inject

class WeatherMessagingService: FirebaseMessagingService() {

    @Inject
    lateinit var  saveUserTokenUseCase: SaveUserTokenUseCase

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onNewToken(token: String) {
        Log.d("TEST-TAG",token)
        saveUserTokenUseCase(token)
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
    }
}