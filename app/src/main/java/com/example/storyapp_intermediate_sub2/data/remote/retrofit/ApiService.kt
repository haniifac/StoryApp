package com.example.storyapp_intermediate_sub2.data.remote.retrofit

import com.example.storyapp_intermediate_sub2.data.remote.response.LoginResponse
import com.example.storyapp_intermediate_sub2.data.remote.response.RegisterResponse
import com.example.storyapp_intermediate_sub2.data.remote.response.StoriesResponse
import com.example.storyapp_intermediate_sub2.data.remote.response.UploadImageResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<RegisterResponse>

    @GET("stories")
    fun fetchStories(
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("location") location: Int?,
    ): Call<StoriesResponse>

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Double?,
        @Part("lon") lon: Double?,
    ): Call<UploadImageResponse>
}