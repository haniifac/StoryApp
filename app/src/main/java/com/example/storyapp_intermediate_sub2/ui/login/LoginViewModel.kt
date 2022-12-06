package com.example.storyapp_intermediate_sub2.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.storyapp_intermediate_sub2.data.remote.ApiConfig
import com.example.storyapp_intermediate_sub2.data.remote.LoginResponse
import com.example.storyapp_intermediate_sub2.data.remote.LoginResult
import com.example.storyapp_intermediate_sub2.data.repository.SessionManager
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    private lateinit var sessionManager: SessionManager

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading get(): LiveData<Boolean> = _isLoading

    var loginResponseMessage = MutableLiveData<String>()
    var loginResult = MutableLiveData<LoginResult?>()

    fun postLogin(email: String, password: String) {
        _isLoading.value = true
        ApiConfig().getApiService()
            .login(email, password) // Bad Practice - Use Dependency Injection
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (response.isSuccessful) {
                        if (responseBody != null) {
                            Log.e(TAG, "onResponse : isSuccessful ${responseBody.message}")
                            loginResult.value = responseBody.loginResult
                            loginResponseMessage.value = responseBody.message
                        }
                    } else {
                        Log.e(TAG, "onResponse : isFailed ${response.message()}")
                        loginResponseMessage.value = response.message()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            })
    }

    fun putSession(sessionManager: SessionManager) {
        this.sessionManager = sessionManager
    }

    suspend fun getToken(): String {
        return sessionManager.getToken()
    }

    fun saveSession(token: String, name: String) {
        viewModelScope.launch {
            sessionManager.saveToken(token)
            sessionManager.saveName(name)
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}