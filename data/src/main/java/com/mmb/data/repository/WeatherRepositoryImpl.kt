package com.mmb.data.repository

import com.mmb.data.datasource.remote.api.WeatherApiService
import com.mmb.data.datasource.remote.util.safeApiCall
import com.mmb.data.mapper.WeatherMapper
import com.mmb.domain.model.CityWeather
import com.mmb.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiService: WeatherApiService,
    private val weatherMapper: WeatherMapper,
) : WeatherRepository {

    override suspend fun getCurrentWeather(city: String): Result<CityWeather> = safeApiCall {
        weatherMapper.map(
            apiService.getCurrentWeather(city)
        )
    }

    override suspend fun searchCity(query: String): Result<List<CityWeather>> = safeApiCall {
        apiService.searchCities(query)
            .map {
                apiService.getCurrentWeather(it.name)
            }
            .map {
                weatherMapper.map(it)
            }
    }
}