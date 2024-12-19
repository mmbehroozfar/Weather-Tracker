package com.mmb.domain.usecase

import com.mmb.domain.repository.CityRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveCityUseCase @Inject constructor(
    private val cityRepository: CityRepository,
) {

    suspend operator fun invoke(city: String) = withContext(Dispatchers.IO) {
        cityRepository.saveCity(
            city
        )
    }
}