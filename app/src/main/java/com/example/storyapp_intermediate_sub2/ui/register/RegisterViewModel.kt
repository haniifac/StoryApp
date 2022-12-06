package com.example.storyapp_intermediate_sub2.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp_intermediate_sub2.data.remote.retrofit.ApiConfig
import com.example.storyapp_intermediate_sub2.data.remote.response.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {
    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading get(): LiveData<Boolean> = _isLoading

    var registerResponse = MutableLiveData<Boolean>()

    fun postRegister(name: String, email: String, password: String) {
        _isLoading.value = true
        ApiConfig().getApiService()
            .register(name, email, password) // Bad Practice - Use Dependency Injection
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (response.isSuccessful) {
                        if (responseBody != null) {
                            Log.e(TAG, "onResponse : isSuccessful ${responseBody.message}")
                            registerResponse.value = responseBody.error
                        }
                    } else {
                        Log.e(TAG, "onResponse : isFailed ${response.message()}")
                        registerResponse.value = true
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            })
    }

    companion object {
        private const val TAG = "RegisterViewModel"
    }
}