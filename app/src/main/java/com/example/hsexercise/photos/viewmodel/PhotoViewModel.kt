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
import com.example.hsexercise.photos.state.StateData
import com.example.hsexercise.photos.state.StateData.State.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class PhotoViewModel(application: Application) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()
    lateinit var photoService: PhotoService

    private val repository: PhotoRepository
    val stateData = MutableLiveData<StateData<List<Photo>>>()
    var isCacheEmpty: Boolean = true

    lateinit var photosList: LiveData<PagedList<Photo>>
    private val pageSize = 5
    lateinit var photoDataSourceFactory: PhotoDataSourceFactory

    init {
        val photoDao = PhotoDatabase.getDatabase(application).photoDao()
        repository = PhotoRepository(photoDao)

//        Database is the single source of truth - so we send success state only after new data cached in database
        repository.photos.observeForever(Observer {
                photos ->
            isCacheEmpty = photos.isNullOrEmpty()
            if(!isCacheEmpty) {
                stateData.postValue(StateData(photos, DONE))
            }
        })
    }

    fun loadFactory() {
        photoDataSourceFactory = PhotoDataSourceFactory(compositeDisposable, photoService)
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setEnablePlaceholders(false)
            .build()
        photosList = LivePagedListBuilder<Int, Photo>(photoDataSourceFactory, config).build()

        stateData.postValue(StateData(state = LOADING))
    }

    fun load() {

        val cachedPhotos = repository.photos.value
        if(!cachedPhotos.isNullOrEmpty()) {
            stateData.postValue(StateData(cachedPhotos, DONE))
            return
        }

        compositeDisposable.add(photoService.listPhotos()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                { photos -> viewModelScope.launch { repository.insertAll(photos) } },
                { error -> stateData.postValue(StateData(state = ERROR, error = error)) }
            )
        )
    }

    fun clear() {
        compositeDisposable.clear()
    }

    fun getState(): LiveData<StateData.State> = Transformations.switchMap<PhotoNetworkDataSource,
            StateData.State>(photoDataSourceFactory.newsDataSourceLiveData, PhotoNetworkDataSource::state)

    fun retry() {
        photoDataSourceFactory.newsDataSourceLiveData.value?.retry()
    }

    fun listIsEmpty(): Boolean {
        return photosList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
