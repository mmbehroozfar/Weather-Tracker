package com.mmb.ui_home

import app.cash.turbine.test
import com.mmb.domain.model.CityWeather
import com.mmb.domain.usecase.GetCurrentCityUseCase
import com.mmb.domain.usecase.GetCurrentWeatherUseCase
import com.mmb.domain.usecase.SaveCityUseCase
import com.mmb.domain.usecase.SearchCitiesUseCase
import com.mmb.ui_home.mapper.ApiErrorMapper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase = mockk()
    private val getCurrentCityUseCase: GetCurrentCityUseCase = mockk()
    private val saveCityUseCase: SaveCityUseCase = mockk()
    private val searchCitiesUseCase: SearchCitiesUseCase = mockk()
    private val errorMapper: ApiErrorMapper = mockk()

    private lateinit var viewModel: HomeViewModel

    private val sampleCityWeather = CityWeather(
        name = "Paris",
        temperature = 31.0,
        humidity = 87,
        uvIndex = 4.0,
        feelsLike = 9.8,
        icon = "https://cdn.weatherapi.com/weather/128x128/night/122.png",
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeViewModel(
            getCurrentWeatherUseCase = getCurrentWeatherUseCase,
            getCurrentCityUseCase = getCurrentCityUseCase,
            saveCityUseCase = saveCityUseCase,
            searchCitiesUseCase = searchCitiesUseCase,
            errorMapper = errorMapper
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun init_whenViewModelStarts_thenFetchesCurrentCityWeather() = runTest {
        coEvery { getCurrentCityUseCase.invoke() } returns sampleCityWeather.name
        coEvery { getCurrentWeatherUseCase(sampleCityWeather.name) } returns Result.success(
            sampleCityWeather
        )

        val state = viewModel.viewState.first()
        assertEquals(sampleCityWeather.name, state.currentCity?.name)
        assertEquals(sampleCityWeather.temperature, state.currentCity?.temperature ?: 0.0, 0.0)
        assertEquals(false, state.isLoading)
    }

    @Test
    fun onCitySelected_thenCityIsSavedAndViewStateIsUpdated() = runTest {
        coEvery { getCurrentCityUseCase.invoke() } returns sampleCityWeather.name
        coEvery { getCurrentWeatherUseCase(sampleCityWeather.name) } returns Result.success(
            sampleCityWeather
        )
        coEvery { saveCityUseCase("NewCity") } returns Unit

        viewModel.viewState.first()

        val newCity = CityWeather(
            name = "NewCity",
            temperature = 31.0,
            humidity = 87,
            uvIndex = 4.0,
            feelsLike = 9.8,
            icon = "https://cdn.weatherapi.com/weather/128x128/night/122.png",
        )
        viewModel.handleViewEvent(HomeViewEvent.OnCitySelected(newCity))

        val state = viewModel.viewState.first()
        assertEquals(newCity.name, state.currentCity?.name)
        assertEquals(null, state.searchQuery)
        assertTrue(state.searchCities.isEmpty())

        coVerify { saveCityUseCase("NewCity") }
    }

    @Test
    fun onSearchQueryChanged_whenQueryIsProvided_thenLoadsCitiesAfterDebounce() = runTest {
        coEvery { getCurrentCityUseCase.invoke() } returns null
        coEvery { searchCitiesUseCase(sampleCityWeather.name) } returns Result.success(
            listOf(
                sampleCityWeather
            )
        )

        viewModel.viewState.first()

        val query = "Paris"
        viewModel.handleViewEvent(HomeViewEvent.OnSearchQueryChanged(query))

        var state = viewModel.viewState.first()
        assertEquals(query, state.searchQuery)
        assertTrue(state.isLoading)

        advanceTimeBy(3000)
        advanceUntilIdle()

        state = viewModel.viewState.first()
        assertEquals(1, state.searchCities.size)
        assertEquals(sampleCityWeather.name, state.searchCities.first().name)
        assertEquals(false, state.isLoading)
    }

    @Test
    fun onRetryRequested_whenNoQuery_thenReloadsWeatherData() = runTest {
        coEvery { getCurrentWeatherUseCase(sampleCityWeather.name) } returns Result.failure(
            Throwable("Network error")
        )
        coEvery { getCurrentCityUseCase() } returns sampleCityWeather.name
        coEvery { errorMapper.map(any()) } returns R.string.no_network_error

        viewModel.viewState.first()

        viewModel.sideEffect.test {
            val errorSideEffect = awaitItem()
            assertTrue(errorSideEffect is HomeSideEffect.ShowError)
            assertEquals(
                R.string.no_network_error,
                (errorSideEffect as HomeSideEffect.ShowError).messageRes
            )
        }

        coEvery { getCurrentWeatherUseCase(sampleCityWeather.name) } returns Result.success(
            sampleCityWeather
        )

        viewModel.handleViewEvent(HomeViewEvent.OnRetryRequested)

        val state = viewModel.viewState.first()
        assertEquals(sampleCityWeather.name, state.currentCity?.name)
    }

    @Test
    fun onRetryRequested_whenQueryExists_thenReloadsCities() = runTest {
        coEvery { getCurrentCityUseCase.invoke() } returns null
        coEvery { searchCitiesUseCase(sampleCityWeather.name) } returns Result.success(emptyList())

        viewModel.viewState.first()

        val query = "Paris"
        viewModel.handleViewEvent(HomeViewEvent.OnSearchQueryChanged(query))

        advanceTimeBy(3000)
        advanceUntilIdle()

        coVerify(exactly = 1) { searchCitiesUseCase(query) }

        viewModel.handleViewEvent(HomeViewEvent.OnRetryRequested)
        advanceUntilIdle()

        coVerify(exactly = 2) { searchCitiesUseCase(query) }
    }
}
