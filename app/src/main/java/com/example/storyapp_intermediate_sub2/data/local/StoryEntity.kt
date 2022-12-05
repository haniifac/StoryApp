package com.example.storyapp_intermediate_sub2.data.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "story")
data class StoryEntity(

    @PrimaryKey
    val id: String,

    val name: String,
    val description: String,

    @ColumnInfo(name = "photo_url")
    var photoUrl: String,

    @ColumnInfo(name = "created_at")
    val createdAt: String,

    val lat: Double?,
    val lon: Double?
) : Parcelable

