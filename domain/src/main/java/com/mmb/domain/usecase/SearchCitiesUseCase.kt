package com.mmb.domain.usecase

import com.mmb.domain.repository.WeatherRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchCitiesUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository,
) {

    suspend operator fun invoke(query: String) = withContext(Dispatchers.IO) {
        weatherRepository.searchCity(query)
    }
}