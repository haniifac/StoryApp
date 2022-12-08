package com.example.storyapp_intermediate_sub2.util

import com.example.storyapp_intermediate_sub2.data.remote.response.ListStoryItem
import com.example.storyapp_intermediate_sub2.data.remote.response.LoginResponse
import com.example.storyapp_intermediate_sub2.data.remote.response.LoginResult
import com.example.storyapp_intermediate_sub2.data.remote.response.StoriesResponse

object Dummy {
    fun generateDummyLoginResponse(): LoginResponse {
        val loginResult = LoginResult(
            userId = "user-vRpFuVHiG4NkADH2",
            name = "synerr",
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXZScEZ1VkhpRzROa0FESDIiLCJpYXQiOjE2NzA0OTI0NjJ9.fWHnJSD5qWTaR7bCZn1Gsa-eieXnQ1i7gswg5OgFaLY"
        )

        return LoginResponse(false, "success", loginResult)
    }

    fun generateDummyStoriesResponse(): StoriesResponse {
        val error = false
        val message = "Stories fetched successfully"
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
        return StoriesResponse(error, message, listStories)
    }
}