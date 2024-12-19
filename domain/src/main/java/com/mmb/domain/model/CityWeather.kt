package com.mmb.domain.model

data class CityWeather(
    val name: String,
    val temperature: Double,
    val humidity: Int,
    val uvIndex: Double,
    val feelsLike: Double,
    val icon: String,
)