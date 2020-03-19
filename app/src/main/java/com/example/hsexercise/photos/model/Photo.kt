package com.example.hsexercise.photos.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class Photo(
    @PrimaryKey
    val id: String,
    val author: String,
    val download_url: String,
    val height: Int,
    val url: String,
    val width: Int
)