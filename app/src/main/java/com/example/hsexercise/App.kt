package com.example.hsexercise

import android.app.Application
import com.example.hsexercise.common.di.DaggerAppComponent

class App: Application() {
    val appComponent = DaggerAppComponent.create()
}