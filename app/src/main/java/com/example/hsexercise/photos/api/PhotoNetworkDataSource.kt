package com.example.hsexercise.photos.api

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.hsexercise.photos.model.Photo
import com.example.hsexercise.photos.state.StateData
import com.example.hsexercise.photos.state.StateData.State.*
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers

class PhotoNetworkDataSource(private val networkService: PhotoService,
                             private val compositeDisposable: CompositeDisposable
):  PageKeyedDataSource<Int, Photo>() {

    var state: MutableLiveData<StateData.State> = MutableLiveData()
    private var retryCompletable: Completable? = null

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Photo>
    ) {
        updateState(LOADING)
        compositeDisposable.add(
            networkService.listPhotos(1, params.requestedLoadSize)
                .subscribe(
                    { response ->
                        updateState(DONE)
                        callback.onResult(response,
                            null,
                            2
                        )
                    },
                    {
                        updateState(ERROR)
                        setRetry(Action { loadInitial(params, callback) })
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) {
        updateState(LOADING)
        compositeDisposable.add(
            networkService.listPhotos(params.key, params.requestedLoadSize)
                .subscribe(
                    { response ->
                        updateState(DONE)
                        callback.onResult(response,
                            params.key + 1
                        )
                    },
                    {
                        updateState(ERROR)
                        setRetry(Action { loadAfter(params, callback) })
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) {

    }

    private fun updateState(state: StateData.State) {
        this.state.postValue(state)
    }

    fun retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(retryCompletable!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe())
        }
    }

    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }

}