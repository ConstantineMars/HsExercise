package com.example.hsexercise.photos

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
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
import com.example.hsexercise.photos.network.PhotoService
import com.example.hsexercise.photos.state.StateData.State.*
import kotlinx.android.synthetic.main.activity_photos.*
import kotlinx.android.synthetic.main.empty.*
import kotlinx.android.synthetic.main.error.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.offline.*
import javax.inject.Inject


class PhotoListActivity : BaseActivity<PhotoViewModel>() {
    override val viewModelClass = PhotoViewModel::class.java
    override val layoutResId = R.layout.activity_photos

    val CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE"

    @Inject
    lateinit var photoService: PhotoService

    override fun onViewLoad(savedInstanceState: Bundle?) {
        (application as App).appComponent.injectPhotoListActivity(this)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.drawable.ic_launcher_foreground)

        val adapter = PhotoListAdapter(this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        viewModel.photoService = photoService
        viewModel.stateData.observe(this, Observer { stateData ->
            stateData?.let {
                if(stateData.data != null) {
                    adapter.setPhotos(stateData.data)
                }

                when(stateData.state) {
                    SUCCESS ->
                        if(stateData.data!!.isEmpty()) {
                            if(!isOnline()) {
                                showOffline()
                            } else {
                                showEmpty()
                            }
                        } else {
                            showContent()
                        }
                    ERROR -> showError()
                    LOADING -> showLoading()
                }

            }
        })

        val intentFilter = IntentFilter()
        intentFilter.addAction(CONNECTIVITY_CHANGE_ACTION)
        registerReceiver(receiver, intentFilter)

        load()
    }

    protected fun load() {
        if(viewModel.isCacheEmpty && !isOnline()) {
            showOffline()
            return
        }

        showLoading()
        viewModel.load()
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        viewModel.clear()
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
        emptyView.isVisible = true
        recyclerView.isVisible = false
        errorView.isVisible = false
        offlineView.isVisible = false
        loadingView.isVisible = false
    }

    private fun showContent() {
        recyclerView.isVisible = true
        emptyView.isVisible = false
        errorView.isVisible = false
        offlineView.isVisible = false
        loadingView.isVisible = false
    }

    private fun showError() {
        errorView.isVisible = true
        emptyView.isVisible = false
        recyclerView.isVisible = false
        offlineView.isVisible = false
        loadingView.isVisible = false
    }

    private fun showOffline() {
        offlineView.isVisible = true
        errorView.isVisible = false
        emptyView.isVisible = false
        recyclerView.isVisible = false
        loadingView.isVisible = false
    }

    private fun showLoading() {
        loadingView.isVisible = true
        offlineView.isVisible = false
        errorView.isVisible = false
        emptyView.isVisible = false
        recyclerView.isVisible = false
    }

    private fun isOnline(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            if (CONNECTIVITY_CHANGE_ACTION == intent.action) {
                load()
            }
        }
    }
}
