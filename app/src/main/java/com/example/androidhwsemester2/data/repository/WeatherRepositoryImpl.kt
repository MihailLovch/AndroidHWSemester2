package com.example.androidhwsemester2.data.repository

import com.example.androidhwsemester2.data.local.repository.WeatherInfoRepository
import com.example.androidhwsemester2.data.mappers.CityWeatherInfoMapper
import com.example.androidhwsemester2.data.mappers.WeatherEntityMapper
import com.example.androidhwsemester2.data.mappers.WeatherResponseMapper
import com.example.androidhwsemester2.data.mappers.mapToWeatherDayEntity
import com.example.androidhwsemester2.data.remote.FirstRetrofit
import com.example.androidhwsemester2.data.remote.SecondRetrofit
import com.example.androidhwsemester2.data.remote.network.OpenWeatherApiService
import com.example.androidhwsemester2.di.dagger.CurrencyDispatchers
import com.example.androidhwsemester2.di.dagger.Dispatcher
import com.example.androidhwsemester2.domain.entity.WeatherDayInfo
import com.example.androidhwsemester2.domain.entity.WeatherEntity
import com.example.androidhwsemester2.domain.repository.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    @FirstRetrofit private val remoteSource: OpenWeatherApiService,
    @SecondRetrofit private val secondRemoteSource: OpenWeatherApiService,
    private val localSource: WeatherInfoRepository,
    @Dispatcher(CurrencyDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,

    ) : WeatherRepository {
    override suspend fun getWeatherInfoByCityName(
        city: String,
        cache: Boolean
    ): WeatherEntity {
        return withContext(ioDispatcher) {
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
        return withContext(ioDispatcher) {
            secondRemoteSource.getFiveDayWeather(
                latitude = lat,
                longitude = long,
                count = count * 8 // тк в респонсе таймстамп 3 часа
            ).list.asSequence()
                .map { it?.main.mapToWeatherDayEntity() }
                .withIndex().filter { it.index % 8 == 0 }
                .map { it.value }
                .toList()
        }
    }

    override suspend fun getWeatherInfoByCords(
        lat: Double,
        lon: Double,
        cache: Boolean
    ): WeatherEntity {
        return withContext(ioDispatcher) {
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
        return withContext(ioDispatcher) {
            localSource.getAllCities().map { (CityWeatherInfoMapper::map)(it) }
        }
    }

    override suspend fun checkCache(lat: Double, lon: Double): WeatherEntity? {
        return withContext(ioDispatcher) {
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
        return withContext(ioDispatcher) {
            localSource.saveCityWeatherInfo((WeatherEntityMapper::map)(model))
        }
    }

    override suspend fun checkCache(cityName: String): WeatherEntity? {
        return withContext(ioDispatcher) {
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