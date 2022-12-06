package com.example.storyapp_intermediate_sub2.data.remote.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.storyapp_intermediate_sub2.data.local.database.StoryDatabase
import com.example.storyapp_intermediate_sub2.data.local.entity.StoryEntity
import com.example.storyapp_intermediate_sub2.data.remote.retrofit.ApiService

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val database: StoryDatabase,
    private val apiService: ApiService
) : RemoteMediator<Int, StoryEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryEntity>
    ): MediatorResult {
        TODO("Not yet implemented")
    }

}