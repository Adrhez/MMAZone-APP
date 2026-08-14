package com.example.mmazone.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.dataStore by preferencesDataStore(name = "user_settings")
class SettingsManager(private val context: Context) {

    private val dataStore = context.dataStore

    companion object {
        val SPOILER_MODE_KEY = booleanPreferencesKey("spoiler_mode")
        val OLED_MODE_KEY = booleanPreferencesKey("oled_mode")
    }

    val spoilerModeFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[SPOILER_MODE_KEY] ?: false
    }

    val oledModeFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[OLED_MODE_KEY] ?: false
    }

    suspend fun setSpoilerMode(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[SPOILER_MODE_KEY] = enabled
        }
    }

    suspend fun setOledMode(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[OLED_MODE_KEY] = enabled
        }
    }
}
