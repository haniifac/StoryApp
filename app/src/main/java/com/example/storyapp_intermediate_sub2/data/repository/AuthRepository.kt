package com.example.storyapp_intermediate_sub2.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.storyapp_intermediate_sub2.data.remote.response.LoginResponse
import com.example.storyapp_intermediate_sub2.data.remote.response.RegisterResponse
import com.example.storyapp_intermediate_sub2.data.remote.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepository(private val mDataStore: SessionManager, private val mApiService: ApiService) {

    fun postLogin(email: String, password: String): LiveData<LoginResponse?> {
        val loginResponse = MutableLiveData<LoginResponse?>()

        mApiService.login(email, password)
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

    fun postRegister(name: String, email: String, password: String): LiveData<Boolean> {
        val registerResponse = MutableLiveData<Boolean>()

        mApiService.register(name, email, password)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    val responseBody = response.body()
                    if (response.isSuccessful) {
                        if (responseBody != null) {
//                            Log.e(RegisterViewModel.TAG, "onResponse : isSuccessful ${responseBody.message}")
                            registerResponse.value = responseBody.error
                        }
                    } else {
//                        Log.e(RegisterViewModel.TAG, "onResponse : isFailed ${response.message()}")
                        if (responseBody != null) {
                            registerResponse.value = responseBody.error
                        }
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
//                    Log.e(RegisterViewModel.TAG, "onFailure: ${t.message}")
                    registerResponse.value = false
                }
            })

        return registerResponse
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