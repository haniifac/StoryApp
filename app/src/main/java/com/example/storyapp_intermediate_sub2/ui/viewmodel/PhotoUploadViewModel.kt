package com.example.storyapp_intermediate_sub2.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp_intermediate_sub2.data.remote.ApiConfig
import com.example.storyapp_intermediate_sub2.data.remote.UploadImageResponse
import com.example.storyapp_intermediate_sub2.data.repository.SessionManager
import com.example.storyapp_intermediate_sub2.util.reduceFileImage
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class PhotoUploadViewModel : ViewModel() {
    private lateinit var sessionManager: SessionManager

    private var _uploadResponse = MutableLiveData<UploadImageResponse>()
    val uploadResponse: LiveData<UploadImageResponse> = _uploadResponse

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading get(): LiveData<Boolean> = _isLoading


    fun uploadImage(getFile: File, desc: String, token: String) {
        _isLoading.value = true

        val file = reduceFileImage(getFile as File)

        val description = desc.toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )

        val service = ApiConfig()
        service.setSessionToken(token)

        service.getApiService().uploadImage(imageMultipart, description)
            .enqueue(object : Callback<UploadImageResponse> {
                override fun onResponse(
                    call: Call<UploadImageResponse>,
                    response: Response<UploadImageResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null && !responseBody.error) {
                            Log.e(TAG, "onResponse : ${responseBody.message}")
                            _uploadResponse.value = response.body()
//                            Toast.makeText(this@MainActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e(TAG, "onResponse : ${response.message()}")
                        _uploadResponse.value = response.body()
//                        Toast.makeText(this@MainActivity, response.message(), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UploadImageResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "onResponse : ${t.message}")
                    _uploadResponse.value =
                        UploadImageResponse(true, t.message ?: "Retrofit instance failed")
//                    Toast.makeText(this@MainActivity, "Gagal instance Retrofit", Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun getToken(): String {
        var token: String = ""
        viewModelScope.launch {
            token = sessionManager.getToken()
        }
        return token
    }

    fun putSession(sessionManager: SessionManager) {
        this.sessionManager = sessionManager
    }

    companion object {
        private const val TAG = "PhotoUploadViewModel"
    }
}