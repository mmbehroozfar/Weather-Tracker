package com.mmb.data.datasource.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class CityDataStore @Inject constructor(
    @ApplicationContext
    private val context: Context,
) {

    suspend fun getCity(): String? = context.dataStore.data
        .map { store ->
            store[CURRENT_CITY_KEY]
        }
        .first()

    suspend fun saveCity(city: String) {
        context.dataStore.edit { store ->
            store[CURRENT_CITY_KEY] = city
        }
    }

    private companion object {

        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "city_store")
        private val CURRENT_CITY_KEY = stringPreferencesKey("CURRENT_CITY_KEY")
    }
}