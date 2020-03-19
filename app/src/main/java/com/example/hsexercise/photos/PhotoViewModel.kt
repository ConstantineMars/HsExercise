package com.example.hsexercise.photos

import android.app.Application
import androidx.annotation.WorkerThread
import androidx.lifecycle.*
import com.example.hsexercise.common.database.PhotoDatabase
import com.example.hsexercise.photos.model.Photo
import com.example.hsexercise.photos.repository.PhotoRepository
import kotlinx.coroutines.launch

class PhotoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PhotoRepository
    val photos: LiveData<List<Photo>>

    init {
        val photoDao = PhotoDatabase.getDatabase(application).photoDao()
        repository = PhotoRepository(photoDao)
        photos = repository.photos
    }

    fun insert(photo: Photo) = viewModelScope.launch {
        repository.insert(photo)
    }

    fun insertAll(photos: List<Photo>) = viewModelScope.launch {
        repository.insertAll(photos)
    }
}
