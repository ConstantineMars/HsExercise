package com.example.hsexercise.photos.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class Photo(
    @PrimaryKey
    val id: String,
    val author: String,
    val url: String,
    val download_url: String,
    val width: Int,
    val height: Int
)