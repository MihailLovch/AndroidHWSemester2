package com.example.androidhwsemester2.domain.usecase

import com.example.androidhwsemester2.domain.entity.WeatherEntity
import com.example.androidhwsemester2.domain.repository.WeatherRepository
import com.example.androidhwsemester2.presentation.model.WeatherDataModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class GetCitiesFromDataBaseUseCaseTest{

    private lateinit var useCase: GetCitiesFromDataBaseUseCase

    @MockK
    private lateinit var mockRepository: WeatherRepository

    @Before
    fun setupData(){
        MockKAnnotations.init(this)
        useCase =  GetCitiesFromDataBaseUseCase(weatherRepository = mockRepository)
    }
    @Test
    fun `Check use case correct maps list of weather entity to weather data model`(){
        val date = Date()
        val mockListWeatherEntity = listOf(
            WeatherEntity(cityName = "test", temperature = 1f, humidity = 1f, pressure = 1f, windSpeed = 1f, iconId = "", lastSearch = date,lat = 1.0, lon = 1.0),
            WeatherEntity(cityName = "test", temperature = 2f, humidity = 2f, pressure = 2f, windSpeed = 1f, iconId = "", lastSearch = date,lat = 1.0, lon = 1.0),
            WeatherEntity(cityName = "test", temperature = 3f, humidity = 3f, pressure = 3f, windSpeed = 1f, iconId = "", lastSearch = date,lat = 1.0, lon = 1.0),
        )
        val expectedListWeatherDataModel = listOf(
            WeatherDataModel(cityName = "test", temperature = 1f, humidity = 1f, pressure = 1f, windSpeed = 1f, iconId = "", lastSearch = date,lat = 1.0, lon = 1.0),
            WeatherDataModel(cityName = "test", temperature = 2f, humidity = 2f, pressure = 2f, windSpeed = 1f, iconId = "", lastSearch = date,lat = 1.0, lon = 1.0),
            WeatherDataModel(cityName = "test", temperature = 3f, humidity = 3f, pressure = 3f, windSpeed = 1f, iconId = "", lastSearch = date,lat = 1.0, lon = 1.0),
        )
        coEvery {
            mockRepository.getAllCities()
        } returns mockListWeatherEntity

        runTest {
            val result = useCase()
            assertEquals(result,expectedListWeatherDataModel)
        }
        coVerify { mockRepository.getAllCities() }
    }
}