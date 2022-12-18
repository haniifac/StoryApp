package com.example.storyapp_intermediate_sub2.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp_intermediate_sub2.data.local.entity.StoryEntity
import com.example.storyapp_intermediate_sub2.data.repository.SessionManager
import com.example.storyapp_intermediate_sub2.data.repository.StoryRepository
import kotlinx.coroutines.launch

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getAllStories(): LiveData<PagingData<StoryEntity>> =
        storyRepository.fetchPagingStory().cachedIn(viewModelScope)

    fun clearSession() {
        viewModelScope.launch {
            storyRepository.clearSession()
        }
    }

    companion object {
        private const val TAG = "FeedViewModel"
    }
}