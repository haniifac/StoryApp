package com.example.storyapp_intermediate_sub2.data.repository

import com.example.storyapp_intermediate_sub2.data.local.database.StoryDatabase
import com.example.storyapp_intermediate_sub2.data.remote.retrofit.ApiService
import com.example.storyapp_intermediate_sub2.data.remote.response.ListStoryItem
import com.example.storyapp_intermediate_sub2.data.remote.response.StoriesResponse
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryRepository(private val mDataStore: SessionManager, private val mApiService: ApiService, mDatabase: StoryDatabase) {
    fun getService() = mApiService

    fun getToken(): String {
        return runBlocking() {
            mDataStore.getToken()
        }
    }
}