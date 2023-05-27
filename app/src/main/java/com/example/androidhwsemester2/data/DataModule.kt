package com.example.androidhwsemester2.data

import com.example.androidhwsemester2.data.local.LocalDataModule
import com.example.androidhwsemester2.data.remote.NetworkModule

import dagger.Module

@Module(includes = [LocalDataModule::class,NetworkModule::class])
class DataModule{
}