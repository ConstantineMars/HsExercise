package com.example.hsexercise.photos

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hsexercise.R
import com.example.hsexercise.App
import com.example.hsexercise.common.BaseActivity
import com.example.hsexercise.photos.network.PhotoService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class PhotoListActivity : BaseActivity<FeatureViewModel>() {
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
        compositeDisposable.add(photoService.listRepos()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                { photos -> Timber.d("photos size = %d", photos.size) },
                { error -> Timber.e(error) }
            )
        )

        val adapter = PhotoListAdapter(this)
        viewModel.photos.observe(this, Observer { words ->
            words?.let {
                Timber.d( "count: %d", it.size)
                adapter.setPhotos(it)
            }
        })

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}
