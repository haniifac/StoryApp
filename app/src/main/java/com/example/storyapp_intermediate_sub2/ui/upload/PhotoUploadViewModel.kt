package com.example.storyapp_intermediate_sub2.ui.upload

import androidx.lifecycle.ViewModel
import com.example.storyapp_intermediate_sub2.data.repository.StoryRepository
import com.google.android.gms.maps.model.LatLng
import java.io.File

class PhotoUploadViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun uploadImage(getFile: File, desc: String, location : LatLng?) = storyRepository.uploadImage(getFile, desc, location)

    companion object {
        private const val TAG = "PhotoUploadViewModel"
    }
}