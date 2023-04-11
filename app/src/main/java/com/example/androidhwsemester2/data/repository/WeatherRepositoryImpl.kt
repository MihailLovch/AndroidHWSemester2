package com.example.androidhwsemester2.data.repository

import com.example.androidhwsemester2.data.local.repository.WeatherInfoRepository
import com.example.androidhwsemester2.data.mappers.CityWeatherInfoMapper
import com.example.androidhwsemester2.data.mappers.WeatherEntityMapper
import com.example.androidhwsemester2.data.mappers.WeatherResponseMapper
import com.example.androidhwsemester2.data.mappers.mapToWeatherDayEntity
import com.example.androidhwsemester2.data.remote.FirstRetrofit
import com.example.androidhwsemester2.data.remote.SecondRetrofit
import com.example.androidhwsemester2.data.remote.network.OpenWeatherApiService
import com.example.androidhwsemester2.domain.entity.WeatherDayInfo
import com.example.androidhwsemester2.domain.entity.WeatherEntity
import com.example.androidhwsemester2.domain.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    @FirstRetrofit private val remoteSource: OpenWeatherApiService,
    @SecondRetrofit private val secondRemoteSource: OpenWeatherApiService,
    private val localSource: WeatherInfoRepository,

    ) : WeatherRepository {
    override suspend fun getWeatherInfoByCityName(
        city: String,
        cache: Boolean
    ): WeatherEntity {
        return withContext(Dispatchers.IO) {
            if (cache) {
                var model = checkCache(city)
                if (model == null) {
                    model =
                        (WeatherResponseMapper::mapToDomain)(remoteSource.getWeatherByCityName(city = city))
                    localSource.saveCityWeatherInfo((WeatherEntityMapper::map)(model))
                }
                model
            } else {
                (WeatherResponseMapper::mapToDomain)(remoteSource.getWeatherByCityName(city = city))
            }
        }
    }

    override suspend fun getWeatherHistoricalInfoByCords(
        lat: Double,
        long: Double,
        count: Int,
    ): List<WeatherDayInfo> {
        return withContext(Dispatchers.IO) {
            secondRemoteSource.getFiveDayWeather(
                latitude = lat,
                longitude = long,
                count = count
            ).list.map {it?.main }.map {it.mapToWeatherDayEntity() }
        }
    }

    override suspend fun getWeatherInfoByCords(
        lat: Double,
        lon: Double,
        cache: Boolean
    ): WeatherEntity {
        return withContext(Dispatchers.IO) {
            if (cache) {
                var model = checkCache(lat = lat, lon = lon)
                if (model == null) {
                    model = (WeatherResponseMapper::mapToDomain)(
                        remoteSource.getWeatherByCords(latitude = lat, longitude = lon)
                    )
                    localSource.saveCityWeatherInfo((WeatherEntityMapper::map)(model))
                }
                model
            } else {
                (WeatherResponseMapper::mapToDomain)(
                    remoteSource.getWeatherByCords(latitude = lat, longitude = lon)
                )
            }
        }
    }

    override suspend fun getAllCities(): List<WeatherEntity> {
        return withContext(Dispatchers.IO) {
            localSource.getAllCities().map { (CityWeatherInfoMapper::map)(it) }
        }
    }

    override suspend fun checkCache(lat: Double, lon: Double): WeatherEntity? {
        return withContext(Dispatchers.IO) {
            val cityWeatherInfo = localSource.getCityWeatherInfoByCords(lat = lat, lon = lon)
            if (cityWeatherInfo == null) {
                null
            } else if ((Calendar.getInstance().timeInMillis - cityWeatherInfo.lastSearch.time) > CACHE_LIFETIME) {
                null
            } else {
                (CityWeatherInfoMapper::map)(cityWeatherInfo)
            }
        }
    }

    override suspend fun saveCity(model: WeatherEntity): Long {
        return withContext(Dispatchers.IO) {
            localSource.saveCityWeatherInfo((WeatherEntityMapper::map)(model))
        }
    }

    override suspend fun checkCache(cityName: String): WeatherEntity? {
        return withContext(Dispatchers.IO) {
            val cityWeatherInfo = localSource.getCityWeatherInfoByName(cityName)
            if (cityWeatherInfo == null) {
                null
            } else if ((Calendar.getInstance().timeInMillis - cityWeatherInfo.lastSearch.time) > CACHE_LIFETIME) {
                null
            } else {
                (CityWeatherInfoMapper::map)(cityWeatherInfo)
            }
        }
    }

    private companion object {
        const val CACHE_LIFETIME = 60 * 1000
    }
}