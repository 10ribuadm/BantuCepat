package com.falahw.bantucepat

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "vpn_settings")

class DataStoreManager(private val context: Context) {

    companion object {
        private val LICENSE_KEY = stringPreferencesKey("license_key")
        private val IS_ACTIVATED = booleanPreferencesKey("is_activated")
    }

    val licenseKey: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[LICENSE_KEY]
    }

    val isActivated: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_ACTIVATED] ?: false
    }

    suspend fun saveLicense(key: String) {
        context.dataStore.edit { preferences ->
            preferences[LICENSE_KEY] = key
        }
    }

    suspend fun setActivated(activated: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_ACTIVATED] = activated
        }
    }
}
