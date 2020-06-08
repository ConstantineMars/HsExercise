package com.example.hsexercise.photos.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.hsexercise.common.db.PhotoDatabase
import com.example.hsexercise.photos.api.PhotoDataSourceFactory
import com.example.hsexercise.photos.api.PhotoNetworkDataSource
import com.example.hsexercise.photos.model.Photo
import com.example.hsexercise.photos.api.PhotoService
import com.example.hsexercise.photos.data.PhotoRepository
import com.example.hsexercise.photos.state.State
import com.example.hsexercise.photos.state.State.LOADING
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch

class PhotoViewModel(application: Application) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()
    lateinit var photoService: PhotoService
    lateinit var photoDataSourceFactory: PhotoDataSourceFactory
    private val repository: PhotoRepository

    lateinit var photosList: LiveData<PagedList<Photo>>
    var isCacheEmpty: Boolean = true
    private val pageSize = 5

    init {
        val photoDao = PhotoDatabase.getDatabase(application).photoDao()
        repository = PhotoRepository(photoDao)
    }

    fun load() {
        photoDataSourceFactory = PhotoDataSourceFactory(compositeDisposable, photoService)
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setEnablePlaceholders(false)
            .build()
        photosList = LivePagedListBuilder(photoDataSourceFactory, config).build()
        photosList.observeForever { photos -> viewModelScope.launch { repository.insertAll(photos) } }
        photoDataSourceFactory.photosDataSourceLiveData.value?.updateState(LOADING)
    }

    fun clear() {
        compositeDisposable.clear()
    }

    fun getState(): LiveData<State> = Transformations.switchMap(photoDataSourceFactory.photosDataSourceLiveData, PhotoNetworkDataSource::state)

    fun retry() {
        photoDataSourceFactory.photosDataSourceLiveData.value?.retry()
    }

    fun listIsEmpty(): Boolean {
        return photosList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
