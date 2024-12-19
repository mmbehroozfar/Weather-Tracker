package com.mmb.data.datasource.remote.di

import com.mmb.data.datasource.remote.api.WeatherApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideWeatherApiService(retrofit: Retrofit) =
        retrofit.create(WeatherApiService::class.java)

}