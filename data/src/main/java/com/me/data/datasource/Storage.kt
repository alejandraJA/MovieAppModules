package com.me.data.datasource

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.me.domain.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.userDataStore by preferencesDataStore(name = Constants.USER_MEMORY)

class Storage @Inject constructor(
    @ApplicationContext context: Context
) {
    private val dataStore = context.userDataStore

    fun getStringFlow(key: String): Flow<String?> =
        dataStore.data
            .map { preferences -> preferences[stringPreferencesKey(key)] }
            .distinctUntilChanged()

    suspend fun setString(key: String, value: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = value
        }
    }

    suspend fun setStrings(values: Map<String, String>) {
        dataStore.edit { preferences ->
            values.forEach { (key, value) ->
                preferences[stringPreferencesKey(key)] = value
            }
        }
    }

    suspend fun getString(key: String): String? =
        dataStore.data.first()[stringPreferencesKey(key)]
}
