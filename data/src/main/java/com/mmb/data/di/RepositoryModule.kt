package com.mmb.data.di

import com.mmb.data.repository.CityRepositoryImpl
import com.mmb.data.repository.WeatherRepositoryImpl
import com.mmb.domain.repository.CityRepository
import com.mmb.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsWeatherRepository(impl: WeatherRepositoryImpl): WeatherRepository

    @Binds
    @Singleton
    abstract fun bindsCityRepository(impl: CityRepositoryImpl): CityRepository
}