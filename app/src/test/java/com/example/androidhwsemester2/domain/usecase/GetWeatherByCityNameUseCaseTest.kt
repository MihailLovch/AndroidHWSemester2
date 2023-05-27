package com.example.androidhwsemester2.domain.usecase

import com.example.androidhwsemester2.domain.entity.WeatherEntity
import com.example.androidhwsemester2.domain.repository.WeatherRepository
import com.example.androidhwsemester2.presentation.model.WeatherDataModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
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
class GetWeatherByCityNameUseCaseTest {


    private lateinit var useCase: GetWeatherByCityNameUseCase

    @MockK
    private lateinit var mockRepository: WeatherRepository

    @Before
    fun setupData() {
        MockKAnnotations.init(this)
        useCase = GetWeatherByCityNameUseCase(mockRepository)
    }

    @Test
    fun `Use case returns correct WeatherDataModel`() {
        val name = "Kazan"
        val date = Date()
        val cache = false
        val expectedWeatherDataModel = WeatherDataModel(
            cityName = name,
            temperature = 1f,
            humidity = 1f,
            pressure = 1f,
            windSpeed = 1f,
            iconId = "",
            lastSearch = date,
            lat = 1.0,
            lon = 1.0
        )
        val mockWeatherEntity = mockk<WeatherEntity> {
            every {cityName} returns name
            every { temperature } returns 1f
            every { humidity } returns 1f
            every { pressure } returns 1f
            every { windSpeed } returns 1f
            every { iconId } returns ""
            every { lastSearch } returns date
            every { lat } returns 1.0
            every { lon } returns 1.0
        }
        coEvery {
            mockRepository.getWeatherInfoByCityName(name,cache)
        } returns mockWeatherEntity

        runTest {
            val result = useCase(name,cache)

            assertEquals(result,expectedWeatherDataModel)
        }
        coVerify { mockRepository.getWeatherInfoByCityName(name,cache) }
    }

    @Test
    fun `Use case throw HttpException with same code if exception occurs in repository`(){
        val name = "Kazan"
        val cache = false
        val expectedException = HttpException(
            Response.error<Any>(
                HttpURLConnection.HTTP_UNAUTHORIZED,
                EMPTY_RESPONSE
            )
        )

        coEvery {
            mockRepository.getWeatherInfoByCityName(name,cache)
        } throws expectedException

        runTest {
            val exception = assertFailsWith<HttpException> {
                useCase(name,cache)
            }
            assertEquals(expectedException.code(),exception.code())
        }

        coVerify { mockRepository.getWeatherInfoByCityName(name,cache) }
    }
}