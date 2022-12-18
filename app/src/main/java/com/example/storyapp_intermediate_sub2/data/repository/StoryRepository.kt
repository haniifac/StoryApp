package com.example.storyapp_intermediate_sub2.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.storyapp_intermediate_sub2.data.local.database.StoryDatabase
import com.example.storyapp_intermediate_sub2.data.local.entity.StoryEntity
import com.example.storyapp_intermediate_sub2.data.remote.mediator.StoryRemoteMediator
import com.example.storyapp_intermediate_sub2.data.remote.response.StoriesResponse
import com.example.storyapp_intermediate_sub2.data.remote.response.UploadImageResponse
import com.example.storyapp_intermediate_sub2.data.remote.retrofit.ApiConfig
import com.example.storyapp_intermediate_sub2.data.remote.retrofit.ApiService
import com.example.storyapp_intermediate_sub2.ui.upload.PhotoUploadViewModel
import com.example.storyapp_intermediate_sub2.util.reduceFileImage
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class StoryRepository(private val mDataStore: SessionManager, private val mApiService: ApiService, private val mDatabase: StoryDatabase) {

    fun fetchPagingStory(): LiveData<PagingData<StoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(mApiService, mDatabase),
            pagingSourceFactory = {
                mDatabase.storyDao().getAllStories()
            }
        ).liveData
    }

    fun getToken(): String {
        return runBlocking() {
            mDataStore.getToken()
        }
    }

    suspend fun clearSession() = mDataStore.clearSession()

    fun fetchStories() : LiveData<StoriesResponse?> {
        val storiesResponse = MutableLiveData<StoriesResponse?>()

        mApiService.fetchStories(null,null,1)
            .enqueue(object : Callback<StoriesResponse> {
                override fun onResponse(
                    call: Call<StoriesResponse>,
                    response: Response<StoriesResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            storiesResponse.value = responseBody
//                            Log.e(TAG, responseBody.listStory.toString())
                        }
                    } else {
                        storiesResponse.value = response.body()
                    }
                }

                override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {}
            })
        return storiesResponse
    }

    fun uploadImage(getFile: File, desc: String, location : LatLng?) : LiveData<UploadImageResponse> {
        val uploadImageResponse = MutableLiveData<UploadImageResponse>()

        val file = reduceFileImage(getFile as File)

        val description = desc.toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )

        val photoLat = location?.latitude
        val photoLon = location?.longitude

        mApiService.uploadImage(imageMultipart, description, photoLat, photoLon)
            .enqueue(object : Callback<UploadImageResponse> {
                override fun onResponse(
                    call: Call<UploadImageResponse>,
                    response: Response<UploadImageResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null && !responseBody.error) {
//                            Log.e(PhotoUploadViewModel.TAG, "onResponse : ${responseBody.message}")
//                            Toast.makeText(this@MainActivity, responseBody.message, Toast.LENGTH_SHORT).show()
//                            _uploadResponse.value = response.body()
                            uploadImageResponse.value = response.body()
                        }
                    } else {
//                        Log.e(PhotoUploadViewModel.TAG, "onResponse : ${response.message()}")
//                        _uploadResponse.value = response.body()
//                        Toast.makeText(this@MainActivity, response.message(), Toast.LENGTH_SHORT).show()
                        uploadImageResponse.value = response.body()
                    }
                }

                override fun onFailure(call: Call<UploadImageResponse>, t: Throwable) {
//                    _isLoading.value = false
//                    Log.e(PhotoUploadViewModel.TAG, "onResponse : ${t.message}")
//                    _uploadResponse.value =
//                        UploadImageResponse(true, t.message ?: "Retrofit instance failed")
//                    Toast.makeText(this@MainActivity, "Gagal instance Retrofit", Toast.LENGTH_SHORT).show()
                }
            })
        return uploadImageResponse
    }

    companion object{
        private const val TAG = "StoryRepository"
    }
}