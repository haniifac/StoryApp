package com.example.storyapp_intermediate_sub2.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp_intermediate_sub2.di.Injection
import com.example.storyapp_intermediate_sub2.ui.login.LoginViewModel
import com.example.storyapp_intermediate_sub2.ui.map.MapsViewModel
import com.example.storyapp_intermediate_sub2.ui.register.RegisterViewModel
import com.example.storyapp_intermediate_sub2.ui.story.StoryViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoryViewModel(Injection.provideStoryRepository(context)) as T
        }else if(modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(Injection.provideAuthRepository(context)) as T
        }else if(modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(Injection.provideAuthRepository(context)) as T
        }else if(modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MapsViewModel(Injection.provideStoryRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}