package com.example.androidhwsemester2.data

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.androidhwsemester2.data.local.LocalDataModule
import com.example.androidhwsemester2.data.remote.NetworkModule

import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module(includes = [LocalDataModule::class,NetworkModule::class])
class DataModule{
}