package com.mmb.data.datasource.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    @SerialName("current")
    val currentWeather: CurrentWeatherResponse,
    @SerialName("location")
    val city: CityResponse,
)



