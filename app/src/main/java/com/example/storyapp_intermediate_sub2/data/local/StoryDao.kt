package com.example.storyapp_intermediate_sub2.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(vararg story: StoryEntity)


    @Query("SELECT * FROM story")
    fun getAllStories(): PagingSource<Int, StoryEntity>

    @Query("DELETE FROM story")
    fun deleteAll()
}