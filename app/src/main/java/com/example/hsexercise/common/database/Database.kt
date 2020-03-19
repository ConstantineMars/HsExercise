package com.example.hsexercise.common.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.hsexercise.photos.database.PhotoDao
import com.example.hsexercise.photos.model.Photo

@Database(entities = [Photo::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun photoDao(): PhotoDao
}