package com.example.androidhwsemester2.data.repository

import com.example.androidhwsemester2.data.local.entity.CityWeatherInfo
import com.example.androidhwsemester2.data.local.repository.WeatherInfoRepository
import com.example.androidhwsemester2.data.remote.model.WeatherAllInfo
import com.example.androidhwsemester2.data.remote.model.WeatherForecastResponse
import com.example.androidhwsemester2.data.remote.model.WeatherResponse
import com.example.androidhwsemester2.data.remote.network.OpenWeatherApiService
import com.example.androidhwsemester2.domain.entity.WeatherDayInfo
import com.example.androidhwsemester2.domain.entity.WeatherEntity
import com.example.androidhwsemester2.domain.repository.WeatherRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherRepositoryImplTest {

    private lateinit var repository: WeatherRepository

    @MockK
    private lateinit var mockRemoteSource: OpenWeatherApiService

    @MockK
    private lateinit var mockSecondRemoteSource: OpenWeatherApiService

    @MockK
    private lateinit var mockLocalSource: WeatherInfoRepository

    private val dispatcher = StandardTestDispatcher()

    private val date = Date()
    private val mockName = "Name"

    private val mockCityWeatherInfo = mockk<CityWeatherInfo> {
        every { cityName } returns mockName
        every { temperature } returns 1f
        every { humidity } returns 1f
        every { pressure } returns 1f
        every { windSpeed } returns 1f
        every { iconId } returns ""
        every { lastSearch } returns date
        every { lat } returns 1.0
        every { lon } returns 1.0
    }
    private val mockWeatherResponse = mockk<WeatherResponse> {
        every { coords } returns mockk {
            every { latitude } returns 1.0
            every { longitude } returns 1.0
        }
        every { weatherList } returns emptyList()
        every { main } returns mockk {
            every { temp } returns 1f
            every { feelsLike } returns 1f
            every { tempMin } returns 1f
            every { tempMax } returns 1f
            every { pressure } returns 1f
            every { humidity } returns 1f
        }
        every { cityName } returns mockName
        every { wind } returns mockk {
            every { speed } returns 1f
            every { deg } returns 1f
            every { gust } returns 1f
        }
    }
    private val expectedWeatherEntity = WeatherEntity(
        cityName = mockName,
        temperature = 1f,
        humidity = 1f,
        pressure = 1f,
        windSpeed = 1f,
        iconId = "",
        lastSearch = date,
        lat = 1.0,
        lon = 1.0
    )

    @Before
    fun setupData() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(dispatcher)
        repository = WeatherRepositoryImpl(
            remoteSource = mockRemoteSource,
            secondRemoteSource = mockSecondRemoteSource,
            localSource = mockLocalSource,
            ioDispatcher = dispatcher
        )
    }

    @Test
    fun `getWeatherInfoByCityName returns weather entity from remote source when cache is false`() {
        runTest {
            val cache = false
            coEvery {
                mockRemoteSource.getWeatherByCityName(mockName)
            } returns mockWeatherResponse


            val result = repository.getWeatherInfoByCityName(mockName, cache)

            assertEquals(expectedWeatherEntity, result.copy(lastSearch = date))
            coVerify { mockRemoteSource.getWeatherByCityName(any()) }

        }
    }

    @Test
    fun `getWeatherInfoByCityName returns weather entity from remote source if cache is true and nothing is found in cache`() {
        runTest {
            val cache = true
            val expectedWeatherEntity = WeatherEntity(
                cityName = mockName,
                temperature = 1f,
                humidity = 1f,
                pressure = 1f,
                windSpeed = 1f,
                iconId = "",
                lastSearch = date,
                lat = 1.0,
                lon = 1.0
            )
            coEvery {
                mockLocalSource.getCityWeatherInfoByName(any())
            } returns null
            coEvery {
                mockLocalSource.saveCityWeatherInfo(any())
            } returns 1
            coEvery {
                mockRemoteSource.getWeatherByCityName(mockName)
            } returns mockWeatherResponse

            val result = repository.getWeatherInfoByCityName(mockName, cache)

            assertEquals(expectedWeatherEntity, result.copy(lastSearch = date))
            coVerify { mockLocalSource.getCityWeatherInfoByName(any()) }
            coVerify { mockLocalSource.saveCityWeatherInfo(any()) }
        }
    }

    @Test
    fun `getWeatherHistoricalInfoByCords correctly maps WeatherForecastResponse to WeatherDayInfo list`() {
        runTest {
            val count = 5
            val mockWeatherList = List(count * 8) {
                mockk<WeatherAllInfo> {
                    every { main } returns mockk {
                        every { temp } returns 1f
                        every { feelsLike } returns 1f
                        every { tempMin } returns 1f
                        every { tempMax } returns 1f
                        every { pressure } returns 1f
                        every { humidity } returns 1f
                    }
                }
            }
            val mockForecastResponse = mockk<WeatherForecastResponse> {
                every { list } returns mockWeatherList
            }
            val expectedWeatherDayInfoList = List(count) {
                WeatherDayInfo(
                    temp = 1f,
                    feelsLike = 1f,
                    tempMin = 1f,
                    tempMax = 1f,
                    pressure = 1f,
                    humidity = 1f
                )
            }
            coEvery {
                mockSecondRemoteSource.getFiveDayWeather(any(), any(), any())
            } returns mockForecastResponse

            val result = repository.getWeatherHistoricalInfoByCords(1.0, 1.0, count)

            assertEquals(result, expectedWeatherDayInfoList)
        }
    }

    @Test
    fun `getWeatherInfoByCords returns weather entity from remote source when cache is false`() {
        runTest {
            val cache = false
            val expectedWeatherEntity = WeatherEntity(
                cityName = mockName,
                temperature = 1f,
                humidity = 1f,
                pressure = 1f,
                windSpeed = 1f,
                iconId = "",
                lastSearch = date,
                lat = 1.0,
                lon = 1.0
            )
            coEvery {
                mockRemoteSource.getWeatherByCords(any(), any())
            } returns mockWeatherResponse


            val result = repository.getWeatherInfoByCords(1.0, 1.0, cache)

            assertEquals(expectedWeatherEntity, result.copy(lastSearch = date))
            coVerify { mockRemoteSource.getWeatherByCords(any(), any()) }

        }
    }

    @Test
    fun `getWeatherInfoByCords returns weather entity from remote source if cache is true and nothing is found in cache`() {
        runTest {
            val cache = true
            val expectedWeatherEntity = WeatherEntity(
                cityName = mockName,
                temperature = 1f,
                humidity = 1f,
                pressure = 1f,
                windSpeed = 1f,
                iconId = "",
                lastSearch = date,
                lat = 1.0,
                lon = 1.0
            )
            coEvery {
                mockLocalSource.getCityWeatherInfoByCords(any(), any())
            } returns null
            coEvery {
                mockLocalSource.saveCityWeatherInfo(any())
            } returns 1
            coEvery {
                mockRemoteSource.getWeatherByCords(any(), any())
            } returns mockWeatherResponse

            val result = repository.getWeatherInfoByCords(1.0, 1.0, cache)

            assertEquals(expectedWeatherEntity, result.copy(lastSearch = date))
            coVerify { mockLocalSource.getCityWeatherInfoByCords(any(), any()) }
            coVerify { mockLocalSource.saveCityWeatherInfo(any()) }
        }
    }

    @Test
    fun `getAllCities correctly maps CityWeatherInfo to WeatherEntity`() {
        runTest {
            val size = 5
            val expectedWeatherEntityList = List(size) { expectedWeatherEntity }
            coEvery {
                mockLocalSource.getAllCities()
            } returns List(size) { mockCityWeatherInfo }

            val result = repository.getAllCities()

            assertEquals(expectedWeatherEntityList, result)
            coVerify { mockLocalSource.getAllCities() }
        }
    }

    @Test
    fun `checkCache maps correctly CityWeatherInfo to WeatherEntity`() {
        runTest {
            coEvery {
                mockLocalSource.getCityWeatherInfoByName(mockName)
            } returns mockCityWeatherInfo

            val result = repository.checkCache(mockName)

            assertEquals(expectedWeatherEntity,result)
            coVerify { mockLocalSource.getCityWeatherInfoByName(mockName) }
        }
    }

    @Test
    fun `saveCity correctly maps weatherEntity to CityWeatherInfo`(){
        runTest {
            val expectedSavedCityWeatherInfo = mockCityWeatherInfo
            val mockWeatherEntity = expectedWeatherEntity
            coEvery {
                mockLocalSource.saveCityWeatherInfo(any())
            } returns 1

            repository.saveCity(mockWeatherEntity)

            coVerify { mockLocalSource.saveCityWeatherInfo(expectedSavedCityWeatherInfo) }
        }
    }
}