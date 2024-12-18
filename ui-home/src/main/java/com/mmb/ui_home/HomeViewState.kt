package com.mmb.ui_home

import com.mmb.domain.model.CityWeather

data class HomeViewState(
    val searchQuery: String? = null,
    val isLoading: Boolean = true,
    val currentCity: CityWeather? = null,
    val searchCities: List<CityWeather> = emptyList(),
) {

    val isSearchMode = !searchQuery.isNullOrEmpty()

}