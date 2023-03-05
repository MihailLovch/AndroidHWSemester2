package com.example.androidhwsemester2.data.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("coord")
    val coords: WeatherCoords? = null,
    @SerializedName("weather")
    val weatherList: List<WeatherInfo>? = null,
    @SerializedName("main")
    val main: WeatherMainInfo? = null,
    @SerializedName("name")
    val cityName: String? = null,
    @SerializedName("wind")
    val wind: Wind? = null
): java.io.Serializable

data class WeatherCoords(
    @SerializedName("lat")
    val latitude: Float?,
    @SerializedName("lon")
    val longitude: Float?
)

data class WeatherInfo(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("main")
    val main: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("icon")
    val icon: String?
)

data class WeatherMainInfo(
    @SerializedName("temp")
    val temp: Float?,
    @SerializedName("feels_like")
    val feelsLike: Float?,
    @SerializedName("temp_min")
    val tempMin: Float?,
    @SerializedName("temp_max")
    val tempMax: Float?,
    @SerializedName("pressure")
    val pressure: Float?,
    @SerializedName("humidity")
    val humidity: Float?
)
data class Wind(
    @SerializedName("speed")
    val speed: Float?,
    @SerializedName("deg")
    val deg: Float?,
    @SerializedName("gust")
    val gust: Float?
)