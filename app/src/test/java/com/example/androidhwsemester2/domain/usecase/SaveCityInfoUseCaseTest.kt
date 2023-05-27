package com.example.androidhwsemester2.domain.usecase

import com.example.androidhwsemester2.domain.entity.mapWeatherDataModel
import com.example.androidhwsemester2.domain.repository.WeatherRepository
import com.example.androidhwsemester2.presentation.model.WeatherDataModel
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
import java.util.Date
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class SaveCityInfoUseCaseTest{

    private lateinit var useCase: SaveCityInfoUseCase

    @MockK
    private lateinit var mockRepository: WeatherRepository

    @Before
    fun setupDate(){
        MockKAnnotations.init(this)
        useCase = SaveCityInfoUseCase(mockRepository)
    }

    @Test
    fun `invoke() returns the saved city ID`() {
        val date = Date()
        val model = WeatherDataModel(
            cityName = "",
            temperature = 0.0f,
            humidity = 0.0f,
            pressure = 0.0f,
            windSpeed = 0.0f,
            iconId = "",
            lastSearch = date,
            lat = 0.0,
            lon = 0.0
        )
        val expectedId = 123L

        coEvery { mockRepository.saveCity(any()) } returns expectedId

        runTest {
            val result = useCase(model)

            assertEquals(expectedId, result)
        }

        coVerify { mockRepository.saveCity(model.mapWeatherDataModel()) }
    }

    @Test
    fun `invoke() throws an exception if repository throws an exception`() {
        val date = Date()
        val model = WeatherDataModel(
            cityName = "",
            temperature = 0.0f,
            humidity = 0.0f,
            pressure = 0.0f,
            windSpeed = 0.0f,
            iconId = "",
            lastSearch = date,
            lat = 0.0,
            lon = 0.0
        )
        val expectedException = HttpException(
            Response.error<Any>(
                HttpURLConnection.HTTP_UNAUTHORIZED,
                EMPTY_RESPONSE
            )
        )

        coEvery { mockRepository.saveCity(any()) } throws expectedException

        runTest {
            val exception = assertFailsWith<HttpException> {
                useCase(model)
            }
            assertEquals(expectedException.code(),exception.code())
        }

        coVerify { mockRepository.saveCity(model.mapWeatherDataModel()) }
    }
}