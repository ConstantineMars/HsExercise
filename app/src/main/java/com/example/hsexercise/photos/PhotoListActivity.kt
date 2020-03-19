package com.example.hsexercise.photos

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.hsexercise.R
import com.example.hsexercise.App
import com.example.hsexercise.common.BaseActivity
import com.example.hsexercise.photos.network.PhotoService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_feature.*
import timber.log.Timber
import javax.inject.Inject

class PhotoListActivity : BaseActivity<PhotoViewModel>() {
    override val viewModelClass = PhotoViewModel::class.java
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
                { photos -> viewModel.insertAll(photos) },
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

        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
//        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.list -> {
                switchToList()
                true
            }
            R.id.grid -> {
                switchToGrid()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun switchToList() {
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun switchToGrid() {
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }
}
