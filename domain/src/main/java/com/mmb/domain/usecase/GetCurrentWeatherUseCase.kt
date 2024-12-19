package com.mmb.domain.usecase

import com.mmb.domain.model.CityWeather
import com.mmb.domain.repository.WeatherRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetCurrentWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository,
) {

    suspend operator fun invoke(city: String): Result<CityWeather> = withContext(Dispatchers.IO) {
        weatherRepository.getCurrentWeather(city)
    }
}