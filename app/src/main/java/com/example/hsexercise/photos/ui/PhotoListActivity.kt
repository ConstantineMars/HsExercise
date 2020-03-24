package com.example.hsexercise.photos.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.hsexercise.App
import com.example.hsexercise.R
import com.example.hsexercise.common.BaseActivity
import com.example.hsexercise.common.api.NetworkUtil
import com.example.hsexercise.common.api.NetworkUtil.isOnline
import com.example.hsexercise.photos.api.PhotoService
import com.example.hsexercise.photos.state.State
import com.example.hsexercise.photos.state.State.*
import com.example.hsexercise.photos.viewmodel.PhotoViewModel
import kotlinx.android.synthetic.main.activity_photos.*
import kotlinx.android.synthetic.main.empty.*
import kotlinx.android.synthetic.main.error.*
import kotlinx.android.synthetic.main.launching.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.offline.*
import javax.inject.Inject

/**
 * Main activity that displays list or grid with photos
 * Toolbar contains menu item for switching between list and grid layout
 * If there is no internet connection - app will either show empty screen (if there are no photos in database)
 * content if everything is ok (data from network or cache), or error screen if there was error in network request
 * Broadcast receiver listens for connectivity changes and reloads list of photos when connectivity established
 */

class PhotoListActivity : BaseActivity<PhotoViewModel>() {
    companion object {
        private const val CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE"
    }

    override val viewModelClass = PhotoViewModel::class.java
    override val layoutResId = R.layout.activity_photos
    private var menu: Menu? = null
    private lateinit var adapter: PhotoListAdapter
    private var justLaunched = true

    @Inject
    lateinit var photoService: PhotoService

    override fun onViewLoad(savedInstanceState: Bundle?) {
        (application as App).appComponent.injectPhotoListActivity(this)

        initActionBar()
        initRecyclerView()
        initViewModel()
        initReceiver()
        initState()
        load()
    }

    private fun initActionBar() {
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.drawable.ic_launcher_foreground)
    }

    private fun initRecyclerView() {
        adapter = PhotoListAdapter(this) {viewModel.retry()}

        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    private fun initViewModel() {
        viewModel.photoService = photoService
        load()
        subscribeToViewModel()
        subscribeToState()
    }

    private fun load() {
        if(viewModel.isCacheEmpty && !isOnline(applicationContext)) {
            showOffline()
        }

        viewModel.load()
    }

    private fun subscribeToState() {
        viewModel.getState().observe(this, Observer {
            if(it == null){
                showEmpty()
            }
            when(it) {
                LOADING -> showLoading()
                ERROR -> if( NetworkUtil.isOnline(this)) {
                    showError() }
                else {
                    showOffline()
                }
                DONE -> showContent()
            }
        })
    }

    private fun subscribeToViewModel() {
        viewModel.photosList.observe(this, Observer {
            adapter.submitList(it)
        })
    }

    private fun initReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(CONNECTIVITY_CHANGE_ACTION)
        registerReceiver(receiver, intentFilter)
    }

    private fun initState() {
        viewModel.getState().observe(this, Observer { state ->
            if (!viewModel.listIsEmpty()) {
                adapter.setState(state ?: State.DONE)
            }
        })
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        viewModel.clear()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    private fun updateMenu() {
        val isInListLayout = recyclerView.layoutManager is LinearLayoutManager

//        Show/hide corresponding menu item makes effect of "changing menu item state"
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
        emptyView.isVisible = true
        recyclerView.isVisible = false
        errorView.isVisible = false
        offlineView.isVisible = false
        loadingView.isVisible = false
        launchingView.isVisible = false
    }

    private fun showContent() {
        recyclerView.isVisible = true
        emptyView.isVisible = false
        errorView.isVisible = false
        offlineView.isVisible = false
        loadingView.isVisible = false
        launchingView.isVisible = false
    }

    private fun showError() {
        errorView.isVisible = true
        emptyView.isVisible = false
        recyclerView.isVisible = false
        offlineView.isVisible = false
        loadingView.isVisible = false
        launchingView.isVisible = false
    }

    private fun showOffline() {
        offlineView.isVisible = true
        errorView.isVisible = false
        emptyView.isVisible = false
        recyclerView.isVisible = false
        loadingView.isVisible = false
        launchingView.isVisible = false
    }

    private fun showLaunchingAnimation() {
        launchingView.isVisible = true
        loadingView.isVisible = false
        offlineView.isVisible = false
        errorView.isVisible = false
        emptyView.isVisible = false
        recyclerView.isVisible = false
    }

    private fun showLoading() {
        if(justLaunched) {
            showLaunchingAnimation()
            justLaunched = false
        } else {
            loadingView.isVisible = true
            launchingView.isVisible = false
        }

        offlineView.isVisible = false
        errorView.isVisible = false
        emptyView.isVisible = false
        recyclerView.isVisible = false
    }

    private val receiver = object : BroadcastReceiver() {

//        Listen for connectivity changes and reload list
        override fun onReceive(context: Context, intent: Intent) {
            if (CONNECTIVITY_CHANGE_ACTION == intent.action) {

//                ViewModel will decide whether load data from database or network
                load()
            }
        }
    }
}
