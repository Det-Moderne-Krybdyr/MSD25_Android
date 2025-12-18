package com.example.msd25_android.ui.user_repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.*

class UserRepository(private val dataStore: DataStore<Preferences>) {
    private companion object {
        val USER_ID = intPreferencesKey("user_id")
        val USER_TOKEN = stringPreferencesKey("user_token")
    }

    val currentToken: Flow<String?> =
        dataStore.data.map { preferences ->
            preferences[USER_TOKEN]
        }

    suspend fun saveUserToken(userToken: String) {
        dataStore.edit { preferences ->
            preferences[USER_TOKEN] = userToken
        }
    }

    val currentUserId: Flow<Int?> =
        dataStore.data.map { preferences ->
            preferences[USER_ID]
        }

    suspend fun saveUserId(userId: Int) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = userId
        }
    }
}