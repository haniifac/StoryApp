package com.example.storyapp_intermediate_sub2.ui.register

import androidx.lifecycle.ViewModel
import com.example.storyapp_intermediate_sub2.data.repository.AuthRepository

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {
    fun postRegister(name: String, email: String, password: String) = authRepository.postRegister(name, email, password)

    companion object {
        private const val TAG = "RegisterViewModel"
    }
}