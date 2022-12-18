package com.example.storyapp_intermediate_sub2.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp_intermediate_sub2.data.remote.response.LoginResponse
import com.example.storyapp_intermediate_sub2.data.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    fun pLogin(email: String, password: String): LiveData<LoginResponse?> = authRepository.postLogin(email, password)

    suspend fun getToken(): String = authRepository.getToken()

    fun saveSession(token: String, name: String) {
        viewModelScope.launch {
            authRepository.saveSession(token, name)
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}


