package com.example.hsexercise.photos

import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.hsexercise.R
import com.example.hsexercise.App
import com.example.hsexercise.common.BaseActivity
import com.example.hsexercise.photos.network.PhotoService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class FeatureActivity : BaseActivity<FeatureViewModel>() {
    override val viewModelClass = FeatureViewModel::class.java
    override val layoutResId = R.layout.activity_feature
    private val compositeDisposable = CompositeDisposable()
    @Inject
    lateinit var photoService: PhotoService

    override fun onBeforeViewLoad(savedInstanceState: Bundle?) {
        super.onBeforeViewLoad(savedInstanceState)
        (application as App).appComponent.injectFeatureActivity(this)
    }

    override fun onViewLoad(savedInstanceState: Bundle?) {
        // todo: write code here
        compositeDisposable.add(photoService.listRepos()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                { photos -> Timber.d("photos size = %d", photos.size) },
                { error -> Timber.e(error) }
            )
        )

        viewModel.photos.observe(this, Observer { words ->
            words?.let {
                Timber.d( "count: %d", it.size)
//                adapter.setWords(it)
            }
        })
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}
