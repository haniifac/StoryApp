package com.example.storyapp_intermediate_sub2.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyapp_intermediate_sub2.data.local.database.StoryDatabase
import com.example.storyapp_intermediate_sub2.data.remote.response.LoginResponse
import com.example.storyapp_intermediate_sub2.data.remote.retrofit.ApiConfig
import com.example.storyapp_intermediate_sub2.data.remote.retrofit.ApiService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepository(private val mDataStore: SessionManager, private val mApiService: ApiService) {

    fun postLogin(email: String, password: String): LiveData<LoginResponse?> {
        val loginResponse = MutableLiveData<LoginResponse?>()

        ApiConfig().getApiService()
            .login(email, password)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    val responseBody = response.body()
                    loginResponse.value = responseBody
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {

                }
            })
        return loginResponse
    }

    suspend fun getToken(): String {
        return mDataStore.getToken()
    }

    suspend fun saveSession(token: String, name: String) {
        mDataStore.saveToken(token)
        mDataStore.saveName(name)
    }

    companion object{
        private const val TAG = "AuthRepository"
    }
}