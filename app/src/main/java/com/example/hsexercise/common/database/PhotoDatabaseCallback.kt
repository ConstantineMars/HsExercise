package com.example.hsexercise.common.database

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.hsexercise.photos.database.PhotoDao
import com.example.hsexercise.photos.model.Photo
import kotlinx.coroutines.CoroutineScope
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
        // Delete all content here.
        photoDao.deleteAll()

        photoDao.insert(Photo("1", "bob", "url1", "url1", 10, 10))
        photoDao.insert(Photo("2", "mike", "url2", "url2", 10, 10))
        photoDao.insert(Photo("3", "frank", "url3", "url3", 10, 10))
    }
}