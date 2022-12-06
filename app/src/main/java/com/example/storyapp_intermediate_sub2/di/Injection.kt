package com.example.storyapp_intermediate_sub2.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.storyapp_intermediate_sub2.data.local.database.StoryDatabase
import com.example.storyapp_intermediate_sub2.data.remote.retrofit.ApiConfig
import com.example.storyapp_intermediate_sub2.data.repository.StoryRepository
import com.example.storyapp_intermediate_sub2.data.repository.SessionManager
import kotlinx.coroutines.runBlocking

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val api = ApiConfig()
        val database = StoryDatabase.getDatabase(context)
        val myDataStore = SessionManager.getInstance(context.dataStore)
        api.setSessionToken(runBlocking {
            myDataStore.getToken()
        })
        return StoryRepository(myDataStore, api.getApiService(), database)
    }
}