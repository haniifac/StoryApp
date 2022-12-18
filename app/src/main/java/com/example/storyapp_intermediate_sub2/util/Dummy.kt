package com.example.storyapp_intermediate_sub2.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.storyapp_intermediate_sub2.data.local.entity.StoryEntity
import com.example.storyapp_intermediate_sub2.data.remote.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

object Dummy {
    fun generateDummyFalseLoginResponse(): LoginResponse {
        val loginResult = LoginResult(
            userId = "user-vRpFuVHiG4NkADH2",
            name = "synerr",
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXZScEZ1VkhpRzROa0FESDIiLCJpYXQiOjE2NzA0OTI0NjJ9.fWHnJSD5qWTaR7bCZn1Gsa-eieXnQ1i7gswg5OgFaLY"
        )

        return LoginResponse(false, "success", loginResult)
    }

    fun generateDummyTrueLoginResponse(): LoginResponse {
        val loginResult = LoginResult(
            userId = "user-vRpFuVHiG4NkADH2",
            name = "synerr",
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXZScEZ1VkhpRzROa0FESDIiLCJpYXQiOjE2NzA0OTI0NjJ9.fWHnJSD5qWTaR7bCZn1Gsa-eieXnQ1i7gswg5OgFaLY"
        )

        return LoginResponse(true, "success", loginResult)
    }

    fun generateDummyTrueRegisterResponse() : RegisterResponse = RegisterResponse(true, "Email is already taken")

    fun generateDummyFalseRegisterResponse() : RegisterResponse = RegisterResponse(false, "user Created")

    fun generateDummyStoryList(): List<StoryEntity> {
        val listStories = mutableListOf<StoryEntity>()

        for (i in 0 until 5) {
            val story = StoryEntity(
                id = "story-kI4cGQdyrEBY8XAk",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1670486409281_nRvy5kcH.jpg",
                createdAt = "2022-12-08T08:00:09.282Z",
                name = "aldioputa",
                description = "Hello Aldi",
                lon = -16.002,
                lat = -10.212
            )

            listStories.add(story)
        }
        return listStories
    }

    fun generateDummyStoryResponseFalse() : StoriesResponse {
        val listStories = mutableListOf<ListStoryItem>()

        for (i in 0 until 5) {
            val story = ListStoryItem(
                id = "story-kI4cGQdyrEBY8XAk",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1670486409281_nRvy5kcH.jpg",
                createdAt = "2022-12-08T08:00:09.282Z",
                name = "aldioputa",
                description = "Hello Aldi",
                lon = -16.002,
                lat = -10.212
            )

            listStories.add(story)
        }

        return StoriesResponse(false, "Stories fetched successfully", listStories)
    }

    fun generateDummyStoryResponseTrue() : StoriesResponse {
        val listStories = mutableListOf<ListStoryItem>()

        for (i in 0 until 5) {
            val story = ListStoryItem(
                id = "story-kI4cGQdyrEBY8XAk",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1670486409281_nRvy5kcH.jpg",
                createdAt = "2022-12-08T08:00:09.282Z",
                name = "aldioputa",
                description = "Hello Aldi",
                lon = -16.002,
                lat = -10.212
            )

            listStories.add(story)
        }

        return StoriesResponse(true, "Stories fetched successfully", listStories)
    }

    fun generateDummyMultipartFile(): MultipartBody.Part {
        val dummyText = "text"
        return MultipartBody.Part.create(dummyText.toRequestBody())
    }

    fun generateDummyDescription(): String {
        return "description"
    }

    fun generateDummyFileUploadResponseFalse(): UploadImageResponse {
        return UploadImageResponse(
            error = false,
            message = "success"
        )
    }

    fun generateDummyFileUploadResponseTrue(): UploadImageResponse {
        return UploadImageResponse(
            error = true,
            message = "\"description\" is required"
        )
    }
}