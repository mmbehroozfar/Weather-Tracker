package com.mmb.domain.usecase

import com.mmb.domain.repository.CityRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetCurrentCityUseCase @Inject constructor(
    private val cityRepository: CityRepository,
) {

    suspend operator fun invoke(): String? =
        withContext(Dispatchers.IO) { cityRepository.getCurrentCity() }
}