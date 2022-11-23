package com.example.storyapp_intermediate_sub2.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SessionManager private constructor(private val dataStore: DataStore<Preferences>) {
    private val USER_NAME = stringPreferencesKey("user_name")
    private val USER_TOKEN = stringPreferencesKey("user_token")

    suspend fun saveName(mUserName: String) {
        dataStore.edit {
            it[USER_NAME] = mUserName
        }
    }

    suspend fun getName(): String {
        val preference = dataStore.data.first()
        return preference[USER_NAME] ?: ""
    }

    suspend fun saveToken(mUserToken: String) {
        dataStore.edit {
            it[USER_TOKEN] = mUserToken
        }
    }

    suspend fun getToken(): String {
        val preference = dataStore.data.first()
        return preference[USER_TOKEN] ?: ""
    }

    fun getTokenLogin(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[USER_TOKEN].toString()
        }
    }

    suspend fun clearSession() {
        dataStore.edit {
            it[USER_NAME] = ""
            it[USER_TOKEN] = ""
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: SessionManager? = null

        fun getInstance(dataStore: DataStore<Preferences>): SessionManager {
            return INSTANCE ?: synchronized(this) {
                val instance = SessionManager(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}