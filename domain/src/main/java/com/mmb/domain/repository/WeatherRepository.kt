package com.mmb.domain.repository

import com.mmb.domain.model.CityWeather

interface WeatherRepository {

    suspend fun getCurrentWeather(city: String): Result<CityWeather>

    suspend fun searchCity(query: String): Result<List<CityWeather>>
}