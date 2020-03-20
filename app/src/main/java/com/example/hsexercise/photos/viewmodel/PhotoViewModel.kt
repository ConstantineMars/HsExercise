package com.example.hsexercise.photos.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.hsexercise.common.database.PhotoDatabase
import com.example.hsexercise.photos.model.Photo
import com.example.hsexercise.photos.network.PhotoService
import com.example.hsexercise.photos.repository.PhotoRepository
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

    init {
        val photoDao = PhotoDatabase.getDatabase(application).photoDao()
        repository = PhotoRepository(photoDao)

//        Database is the single source of truth - so we send success state only after new data cached in database
        repository.photos.observeForever(Observer {
                photos ->
            isCacheEmpty = photos.isNullOrEmpty()
            if(!isCacheEmpty) {
                stateData.postValue(StateData(photos, SUCCESS))
            }
        })

        stateData.postValue(StateData(state = LOADING))
    }

    fun load() {
        val cachedPhotos = repository.photos.value
        if(!cachedPhotos.isNullOrEmpty()) {
            stateData.postValue(StateData(cachedPhotos, SUCCESS))
            return
        }

        compositeDisposable.add(photoService.listRepos()
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
}
