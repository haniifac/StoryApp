package com.example.storyapp_intermediate_sub2.ui.story

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp_intermediate_sub2.data.local.entity.StoryEntity
import com.example.storyapp_intermediate_sub2.data.remote.retrofit.ApiConfig
import com.example.storyapp_intermediate_sub2.data.remote.response.StoriesResponse
import com.example.storyapp_intermediate_sub2.data.repository.SessionManager
import com.example.storyapp_intermediate_sub2.data.repository.StoryRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    private lateinit var sessionManager: SessionManager

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading get(): LiveData<Boolean> = _isLoading

    private var _storiesResponse = MutableLiveData<StoriesResponse?>()
    val storiesResponse: LiveData<StoriesResponse?> get() = _storiesResponse

//    val story: LiveData<PagingData<StoryEntity>> =
//        storyRepository.fetchPagingStory().cachedIn(viewModelScope)

    fun getAllStories(): LiveData<PagingData<StoryEntity>> =
        storyRepository.fetchPagingStory().cachedIn(viewModelScope)

    fun loadFeed(token: String) {
        _isLoading.value = true
        val api = ApiConfig()
        api.setSessionToken(token)
        api.getApiService().fetchStories(null,null,0) // Bad Practice - Use Dependency Injection
            .enqueue(object : Callback<StoriesResponse> {
                override fun onResponse(
                    call: Call<StoriesResponse>,
                    response: Response<StoriesResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            Log.e(TAG, "onResponse : ${responseBody.message}")
                            _storiesResponse.value = responseBody
                        }
                    } else {
                        Log.e(TAG, "onResponse: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            })
    }

    fun getToken(): String {
        var token: String = ""
        viewModelScope.launch {
            token = sessionManager.getToken()
        }
        return token
    }

    fun putSession(sessionManager: SessionManager) {
        this.sessionManager = sessionManager
    }

    fun clearSession(sessionManager: SessionManager) {
        viewModelScope.launch {
            sessionManager.clearSession()
        }
    }

    companion object {
        private const val TAG = "FeedViewModel"
    }
}