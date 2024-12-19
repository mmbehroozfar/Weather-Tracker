package com.mmb.data.datasource.remote.api

import com.mmb.data.datasource.remote.model.CityResponse
import com.mmb.data.datasource.remote.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("q") city: String
    ): WeatherResponse

    @GET("search.json")
    suspend fun searchCities(
        @Query("q") query: String
    ): List<CityResponse>
}