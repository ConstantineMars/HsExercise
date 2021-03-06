package com.example.hsexercise.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hsexercise.App

abstract class BaseActivity<VM : ViewModel> : AppCompatActivity() {
    protected abstract val viewModelClass: Class<VM>
    protected abstract val layoutResId: Int
    protected lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBeforeViewLoad(savedInstanceState)

        setContentView(layoutResId)
        viewModel = ViewModelProvider(this).get(viewModelClass)
        onViewLoad(savedInstanceState)
    }
    open fun onBeforeViewLoad(savedInstanceState: Bundle?) {
        // Intentionally empty so that subclasses can override if necessary
    }

    open fun onViewLoad(savedInstanceState: Bundle?) {
        // Intentionally empty so that subclasses can override if necessary
    }
}
