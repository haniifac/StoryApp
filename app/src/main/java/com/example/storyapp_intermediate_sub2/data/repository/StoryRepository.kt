package com.example.storyapp_intermediate_sub2.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.example.storyapp_intermediate_sub2.data.local.database.StoryDatabase
import com.example.storyapp_intermediate_sub2.data.local.entity.StoryEntity
import com.example.storyapp_intermediate_sub2.data.remote.mediator.StoryRemoteMediator
import com.example.storyapp_intermediate_sub2.data.remote.response.StoriesResponse
import com.example.storyapp_intermediate_sub2.data.remote.retrofit.ApiService
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryRepository(private val mDataStore: SessionManager, private val mApiService: ApiService, private val mDatabase: StoryDatabase) {

    fun fetchPagingStory(): LiveData<PagingData<StoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(mApiService, mDatabase),
            pagingSourceFactory = {
                mDatabase.storyDao().getAllStories()
            }
        ).liveData
    }

    fun getToken(): String {
        return runBlocking() {
            mDataStore.getToken()
        }
    }

    suspend fun clearSession() = mDataStore.clearSession()

    fun fetchStories() : LiveData<StoriesResponse?> {
        val storiesResponse = MutableLiveData<StoriesResponse?>()

        mApiService.fetchStories(null,null,1)
            .enqueue(object : Callback<StoriesResponse> {
                override fun onResponse(
                    call: Call<StoriesResponse>,
                    response: Response<StoriesResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            storiesResponse.value = responseBody
//                            Log.e(TAG, responseBody.listStory.toString())
                        }
                    } else {
                        storiesResponse.value = response.body()
                    }
                }

                override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {}
            })
        return storiesResponse
    }

    companion object{
        private const val TAG = "StoryRepository"
    }
}