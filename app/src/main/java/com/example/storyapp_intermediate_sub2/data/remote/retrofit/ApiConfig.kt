package com.example.storyapp_intermediate_sub2.data.remote.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.storyapp_intermediate_sub2.BuildConfig
import com.example.storyapp_intermediate_sub2.data.remote.interceptor.TokenInterceptor

class ApiConfig {
    private lateinit var sessionToken: String

    fun getApiService(): ApiService {
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }

        val client = if (this::sessionToken.isInitialized) {
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(TokenInterceptor("Bearer", sessionToken))
                .build()
        } else {
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.STORY_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }

    fun setSessionToken(token: String) {
        sessionToken = token
    }
}