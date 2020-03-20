package com.example.hsexercise.common.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.hsexercise.common.db.PhotoDatabase.Companion.DATABASE_VERSION
import com.example.hsexercise.photos.db.PhotoDao
import com.example.hsexercise.photos.model.Photo

@Database(entities = [Photo::class], version = DATABASE_VERSION)
abstract class PhotoDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao

    companion object {
        private const val DATABASE_NAME = "photo_database"
        const val DATABASE_VERSION = 1

        @Volatile
        var INSTANCE: PhotoDatabase? = null

        fun getDatabase(context: Context): PhotoDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PhotoDatabase::class.java,
                    DATABASE_NAME
                )
                    .addCallback(PhotoDatabaseCallback())
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}