package com.example.storyapp_intermediate_sub2.ui.map

import androidx.lifecycle.ViewModel
import com.example.storyapp_intermediate_sub2.data.repository.StoryRepository

class MapsViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun loadFeed() = storyRepository.fetchStories()

    companion object {
        private const val TAG = "MapsViewModel"
    }
}