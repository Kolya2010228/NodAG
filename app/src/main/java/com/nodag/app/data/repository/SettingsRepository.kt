package com.nodag.app.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.nodag.app.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/**
 * Repository для управления API ключами и настройками
 */
class SettingsRepository(private val context: Context) {
    
    private val json = Json { ignoreUnknownKeys = true }
    
    // API Keys
    val apiKeysFlow: Flow<List<ApiKey>> = context.dataStore.data.map { preferences ->
        val keysJson = preferences[stringPreferencesKey("api_keys")] ?: "[]"
        json.decodeFromString(keysJson)
    }
    
    // Settings
    val settingsFlow: Flow<AppSettings> = context.dataStore.data.map { preferences ->
        val settingsJson = preferences[stringPreferencesKey("settings")] ?: "{}"
        try {
            json.decodeFromString(settingsJson)
        } catch (e: Exception) {
            AppSettings()
        }
    }
    
    suspend fun saveApiKey(apiKey: ApiKey) {
        context.dataStore.edit { preferences ->
            val keys = apiKeysFlow.value.toMutableList()
            val existingIndex = keys.indexOfFirst { it.serviceType == apiKey.serviceType }
            if (existingIndex >= 0) {
                keys[existingIndex] = apiKey
            } else {
                keys.add(apiKey)
            }
            preferences[stringPreferencesKey("api_keys")] = json.encodeToString(keys)
        }
    }
    
    suspend fun deleteApiKey(serviceType: ServiceType) {
        context.dataStore.edit { preferences ->
            val keys = apiKeysFlow.value.filter { it.serviceType != serviceType }
            preferences[stringPreferencesKey("api_keys")] = json.encodeToString(keys)
        }
    }
    
    suspend fun saveSettings(settings: AppSettings) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey("settings")] = json.encodeToString(settings)
        }
    }
    
    suspend fun getApiKey(serviceType: ServiceType): String? {
        return apiKeysFlow.value.find { it.serviceType == serviceType && it.isActive }?.key
    }
}
