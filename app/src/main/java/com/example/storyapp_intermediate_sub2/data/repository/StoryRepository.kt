package com.example.storyapp_intermediate_sub2.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.storyapp_intermediate_sub2.data.local.database.StoryDatabase
import com.example.storyapp_intermediate_sub2.data.local.entity.StoryEntity
import com.example.storyapp_intermediate_sub2.data.remote.paging.StoryRemoteMediator
import com.example.storyapp_intermediate_sub2.data.remote.retrofit.ApiService
import com.example.storyapp_intermediate_sub2.data.remote.response.ListStoryItem
import com.example.storyapp_intermediate_sub2.data.remote.response.StoriesResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class StoryRepository(private val mDataStore: SessionManager, private val mApiService: ApiService, private val mDatabase: StoryDatabase) {
    fun getService() = mApiService

    fun fetchPagingStory(): LiveData<PagingData<StoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(mDatabase, mApiService),
            pagingSourceFactory = {
//                QuotePagingSource(apiService)
                mDatabase.storyDao().getAllStories()
            }
        ).liveData
    }

    fun getToken(): String {
        return runBlocking() {
            mDataStore.getToken()
        }
    }
}