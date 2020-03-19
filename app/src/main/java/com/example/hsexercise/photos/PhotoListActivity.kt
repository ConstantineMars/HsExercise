package com.example.hsexercise.photos

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.isVisible
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
import kotlinx.android.synthetic.main.empty.*
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

                if(it.isEmpty()) {
                    showEmpty()
                } else {
                    adapter.setPhotos(it)
                }
            }
        })

        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    var menu: Menu? = null
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    private fun updateMenu() {
        val isInListLayout = recyclerView.layoutManager is LinearLayoutManager

        menu?.findItem(R.id.list)?.isVisible = !isInListLayout
        menu?.findItem(R.id.grid)?.isVisible = isInListLayout
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        updateMenu()

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.list -> {
                switchToList()
                updateMenu()
                true
            }
            R.id.grid -> {
                switchToGrid()
                updateMenu()
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

    private fun showEmpty() {
        recyclerView.isVisible = false
        empty.isVisible = true
    }

    private fun hideEmpty() {
        recyclerView.isVisible = true
        empty.isVisible = false
    }
}
