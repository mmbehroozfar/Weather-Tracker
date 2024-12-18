package com.mmb.ui_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmb.domain.model.CityWeather
import com.mmb.domain.usecase.GetCurrentCityUseCase
import com.mmb.domain.usecase.GetCurrentWeatherUseCase
import com.mmb.domain.usecase.SaveCityUseCase
import com.mmb.domain.usecase.SearchCitiesUseCase
import com.mmb.ui_home.HomeViewEvent.OnCitySelected
import com.mmb.ui_home.HomeViewEvent.OnRetryRequested
import com.mmb.ui_home.HomeViewEvent.OnSearchQueryChanged
import com.mmb.ui_home.mapper.ApiErrorMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getCurrentCityUseCase: GetCurrentCityUseCase,
    private val saveCityUseCase: SaveCityUseCase,
    private val searchCitiesUseCase: SearchCitiesUseCase,
    private val errorMapper: ApiErrorMapper,
) : ViewModel() {

    private val _viewState = MutableStateFlow(HomeViewState())
    val viewState = _viewState
        .onStart {
            loadWeatherData()
            observerSearchQueryChanges()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            HomeViewState()
        )

    private val _sideEffect = Channel<HomeSideEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    fun handleViewEvent(event: HomeViewEvent) {
        when (event) {
            is OnCitySelected -> handleOnCitySelected(event.city)
            is OnSearchQueryChanged -> handleOnSearchQueryChanged(event.query)
            OnRetryRequested -> handleOnRetryRequested()
        }
    }

    private fun loadWeatherData() = viewModelScope.launch {
        getCurrentCityUseCase()?.let { city ->
            getCurrentWeatherUseCase(city)
                .onSuccess { cityWeather ->
                    _viewState.update {
                        it.copy(
                            currentCity = cityWeather,
                            isLoading = false
                        )
                    }
                }
                .onFailure { throwable ->
                    _viewState.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    _sideEffect.send(HomeSideEffect.ShowError(errorMapper.map(throwable)))
                }
        } ?: run {
            _viewState.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observerSearchQueryChanges() {
        _viewState
            .map {
                it.searchQuery
            }
            .debounce(3000)
            .filterNotNull()
            .distinctUntilChanged()
            .onEach {
                loadCities(it)
            }
            .launchIn(viewModelScope)
    }

    private fun loadCities(query: String) = viewModelScope.launch {
        searchCitiesUseCase(query)
            .onSuccess { cities ->
                _viewState.update {
                    it.copy(
                        isLoading = false,
                        searchCities = cities
                    )
                }
            }
            .onFailure { throwable ->
                _viewState.update {
                    it.copy(
                        isLoading = false,
                        searchCities = emptyList()
                    )
                }
                _sideEffect.send(HomeSideEffect.ShowError(errorMapper.map(throwable)))
            }
    }

    private fun handleOnSearchQueryChanged(newQuery: String) {
        viewModelScope.launch {
            _viewState.update { state ->
                state.copy(
                    searchQuery = newQuery.trim().takeIf { it.isNotBlank() },
                    isLoading = newQuery.isNotBlank()
                )
            }
        }
    }

    private fun handleOnCitySelected(city: CityWeather) {
        viewModelScope.launch {
            saveCityUseCase(city.name)
            _viewState.update {
                it.copy(
                    searchQuery = null,
                    currentCity = city,
                    searchCities = emptyList(),
                )
            }
        }
    }

    private fun handleOnRetryRequested() {
        _viewState.update {
            it.copy(
                isLoading = true
            )
        }
        _viewState.value.searchQuery?.let {
            loadCities(it)
        } ?: run {
            loadWeatherData()
        }
    }
}