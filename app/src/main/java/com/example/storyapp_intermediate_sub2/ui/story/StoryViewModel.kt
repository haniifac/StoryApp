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

    fun getAllStories(): LiveData<PagingData<StoryEntity>> =
        storyRepository.fetchPagingStory().cachedIn(viewModelScope)

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