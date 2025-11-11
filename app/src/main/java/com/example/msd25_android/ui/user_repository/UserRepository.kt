package com.example.msd25_android.ui.user_repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.*

class UserRepository(private val dataStore: DataStore<Preferences>) {
    private companion object {
        val PHONE_NUMBER = stringPreferencesKey("phone_number")
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

    val currentPhoneNumber: Flow<String?> =
        dataStore.data.map { preferences ->
            preferences[PHONE_NUMBER]
        }

    suspend fun savePhoneNumber(phoneNumber: String) {
        dataStore.edit { preferences ->
            preferences[PHONE_NUMBER] = phoneNumber
        }
    }
}