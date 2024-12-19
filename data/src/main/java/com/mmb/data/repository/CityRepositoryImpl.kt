package com.mmb.data.repository

import com.mmb.data.datasource.local.CityDataStore
import com.mmb.domain.repository.CityRepository
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(
    private val dataStore: CityDataStore,
) : CityRepository {

    override suspend fun getCurrentCity(): String? = dataStore.getCity()

    override suspend fun saveCity(city: String) {
        dataStore.saveCity(city)
    }
}