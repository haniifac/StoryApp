package com.example.storyapp_intermediate_sub2.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.storyapp_intermediate_sub2.data.remote.ApiConfig
import com.example.storyapp_intermediate_sub2.data.repository.FeedRepository
import com.example.storyapp_intermediate_sub2.data.repository.SessionManager
import kotlinx.coroutines.runBlocking

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

object Injection {
    fun provideRepository(context: Context): FeedRepository {
        val api = ApiConfig()
        val myDataStore = SessionManager.getInstance(context.dataStore)
        api.setSessionToken(runBlocking {
            myDataStore.getToken()
        })
        return FeedRepository(myDataStore, api.getApiService())
    }
}