package com.mmb.domain.repository

interface CityRepository {

    suspend fun getCurrentCity(): String?

    suspend fun saveCity(city: String)

}