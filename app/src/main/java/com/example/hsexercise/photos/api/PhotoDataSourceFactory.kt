package com.example.hsexercise.photos.api

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.hsexercise.photos.model.Photo
import io.reactivex.disposables.CompositeDisposable

class PhotoDataSourceFactory (
    private val compositeDisposable: CompositeDisposable,
    private val networkService: PhotoService)
    : DataSource.Factory<Int, Photo>() {

    val newsDataSourceLiveData = MutableLiveData<PhotoNetworkDataSource>()

    override fun create(): DataSource<Int, Photo> {
        val newsDataSource = PhotoNetworkDataSource(networkService, compositeDisposable)
        newsDataSourceLiveData.postValue(newsDataSource)
        return newsDataSource
    }
}