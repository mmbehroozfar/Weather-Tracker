package com.mmb.data.datasource.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherResponse(
    @SerialName("temp_c")
    val temperature: Double,
    @SerialName("condition")
    val condition: WeatherConditionResponse,
    @SerialName("humidity")
    val humidity: Int,
    @SerialName("uv")
    val uvIndex: Double,
    @SerialName("feelslike_c")
    val feelsLike: Double
)