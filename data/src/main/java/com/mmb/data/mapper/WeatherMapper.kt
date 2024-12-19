package com.mmb.data.mapper

import com.mmb.data.datasource.remote.model.WeatherResponse
import com.mmb.domain.model.CityWeather
import javax.inject.Inject

class WeatherMapper @Inject constructor() {

    fun map(from: WeatherResponse) = with(from) {
        CityWeather(
            name = city.name,
            temperature = currentWeather.temperature,
            humidity = currentWeather.humidity,
            uvIndex = currentWeather.uvIndex,
            feelsLike = currentWeather.feelsLike,
            icon = "https://${currentWeather.condition.iconUrl.replace("64x64", "128x128")}",
        )
    }
}