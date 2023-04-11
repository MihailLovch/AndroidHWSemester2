package com.example.androidhwsemester2.data.remote.model


import com.google.gson.annotations.SerializedName

data class WeatherForecastResponse(
    @SerializedName("list")
    val list: List<WeatherAllInfo?>,
)

data class WeatherAllInfo(
    @SerializedName("main")
    val main: WeatherMainInfo?
)