package com.example.hsexercise

import android.app.Application
import com.example.hsexercise.common.di.AppComponent
import com.example.hsexercise.common.di.DaggerAppComponent
import com.example.hsexercise.common.di.modules.ApplicationModule

class App: Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .applicationModule(ApplicationModule(applicationContext))
            .build()
    }
}