package com.example.storyapp_intermediate_sub2.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.storyapp_intermediate_sub2.data.local.database.StoryDatabase
import com.example.storyapp_intermediate_sub2.data.local.entity.StoryEntity
import com.example.storyapp_intermediate_sub2.data.remote.mediator.StoryRemoteMediator
import com.example.storyapp_intermediate_sub2.data.remote.retrofit.ApiService
import kotlinx.coroutines.runBlocking



class StoryRepository(private val mDataStore: SessionManager, private val mApiService: ApiService, private val mDatabase: StoryDatabase) {
    fun getService() = mApiService

    fun fetchPagingStory(): LiveData<PagingData<StoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(mApiService, mDatabase),
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