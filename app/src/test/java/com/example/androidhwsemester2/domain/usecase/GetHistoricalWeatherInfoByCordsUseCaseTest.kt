package com.example.androidhwsemester2.domain.usecase

import com.example.androidhwsemester2.domain.entity.WeatherDayInfo
import com.example.androidhwsemester2.domain.repository.WeatherRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.internal.EMPTY_RESPONSE
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.net.HttpURLConnection
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class GetHistoricalWeatherInfoByCordsUseCaseTest {
    private lateinit var useCase: GetHistoricalWeatherInfoByCordsUseCase

    @MockK
    private lateinit var mockRepository: WeatherRepository

    @Before
    fun setupData() {
        MockKAnnotations.init(this)
        useCase = GetHistoricalWeatherInfoByCordsUseCase(mockRepository)
    }

    @Test
    fun `Check use case return weather day info list`() {
        val expectedWeatherList = listOf(
            WeatherDayInfo(
                temp = 1f,
                feelsLike = 1f,
                tempMin = 1f,
                tempMax = 1f,
                pressure = 1f,
                humidity = 1f
            )
            ,
            WeatherDayInfo(
                temp = 3f,
                feelsLike = 42f,
                tempMin = 1f,
                tempMax = 1f,
                pressure = 1f,
                humidity = 1f
            ),
            WeatherDayInfo(
                temp = 2f,
                feelsLike = 12f,
                tempMin = 1f,
                tempMax = 1f,
                pressure = 1f,
                humidity = 1f
            ),
        )
        val lat = 1.0
        val long = 1.0
        val count = 1

        coEvery {
            mockRepository.getWeatherHistoricalInfoByCords(lat, long, count)
        } returns expectedWeatherList

        runTest {
            val result = useCase(lat, long, count)

            assertEquals(expectedWeatherList, result)
        }
        coVerify { mockRepository.getWeatherHistoricalInfoByCords(lat,long,count) }
    }

    @Test
    fun `Use case throws HttpException with same code if exception occurs in repository`() {
        val lat = 1.0
        val long = 1.0
        val count = 1
        val expectedException = HttpException(
            Response.error<Any>(
                HttpURLConnection.HTTP_UNAUTHORIZED,
                EMPTY_RESPONSE
            )
        )

        coEvery {
            mockRepository.getWeatherHistoricalInfoByCords(lat, long, count)
        } throws expectedException

        runTest {
            val result = assertFailsWith<HttpException>{
                useCase(lat,long,count)
            }
            assertEquals(result.code(),expectedException.code())
        }
        coVerify { mockRepository.getWeatherHistoricalInfoByCords(lat,long,count) }
    }
}