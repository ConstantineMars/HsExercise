package com.example.hsexercise.photos.repository

import androidx.lifecycle.LiveData
import com.example.hsexercise.photos.database.PhotoDao
import com.example.hsexercise.photos.model.Photo

class PhotoRepository(private val photoDao: PhotoDao) {
    val photos: LiveData<List<Photo>> = photoDao.getAllPhotos()

    suspend fun insert(photo: Photo) {
        photoDao.insert(photo)
    }
}