package com.mmb.ui_home

import com.mmb.domain.model.CityWeather

sealed interface HomeViewEvent {
    data class OnSearchQueryChanged(val query: String) : HomeViewEvent
    data class OnCitySelected(val city: CityWeather) : HomeViewEvent
    data object OnRetryRequested : HomeViewEvent
}