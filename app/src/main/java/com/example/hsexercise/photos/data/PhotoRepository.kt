package com.example.hsexercise.photos.data

import androidx.lifecycle.LiveData
import com.example.hsexercise.photos.db.PhotoDao
import com.example.hsexercise.photos.model.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Common Repository pattern
 * TODO: Actually, this should include work with both network and db data sources - postopned as not critical task
 */

class PhotoRepository(private val photoDao: PhotoDao) {
    val photos: LiveData<List<Photo>> = photoDao.getAllPhotos()

    suspend fun insert(photo: Photo) {
        photoDao.insert(photo)
    }

    suspend fun insertAll(photos: List<Photo>) {
        withContext(Dispatchers.IO) {
            photoDao.insertAll(photos)
        }
    }
}