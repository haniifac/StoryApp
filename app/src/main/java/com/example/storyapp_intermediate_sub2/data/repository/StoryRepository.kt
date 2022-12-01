package com.example.storyapp_intermediate_sub2.data.repository

import com.example.storyapp_intermediate_sub2.data.local.StoryDatabase
import com.example.storyapp_intermediate_sub2.data.remote.ApiService
import com.example.storyapp_intermediate_sub2.data.remote.ListStoryItem
import com.example.storyapp_intermediate_sub2.data.remote.StoriesResponse
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryRepository(private val mDataStore: SessionManager, private val mApiService: ApiService, mDatabase: StoryDatabase) {
    fun getService() = mApiService

    fun fetchFeed(): StoriesResponse {
        var myError: Boolean = true
        var myMessage: String = ""
        var myList: List<ListStoryItem?>? = null

        mApiService.fetchStories(null,null,null) // Bad Practice - Use Dependency Injection
            .enqueue(object : Callback<StoriesResponse> {
                override fun onResponse(
                    call: Call<StoriesResponse>,
                    response: Response<StoriesResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            myError = responseBody.error
                            myMessage = responseBody.message
                            myList = responseBody.listStory
                        }
                    } else {
                        myError = true
                        myMessage = response.message()
                        myList = null

                    }
                }

                override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                    myError = true
                    myMessage = t.message.toString()
                    myList = null
                }
            })
        return StoriesResponse(myError, myMessage, myList)
    }

    fun getToken(): String {
        return runBlocking() {
            mDataStore.getToken()
        }
    }
}