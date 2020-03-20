package com.example.hsexercise.common.db

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.hsexercise.photos.db.PhotoDao
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PhotoDatabaseCallback: RoomDatabase.Callback() {

    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)
        PhotoDatabase.INSTANCE?.let { database ->
            GlobalScope.launch {
                populateDatabase(database.photoDao())
            }
        }
    }

    suspend fun populateDatabase(photoDao: PhotoDao) {
        photoDao.deleteAll()
    }
}