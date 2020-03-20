package com.example.hsexercise

import android.app.Application
import com.example.hsexercise.common.di.AppComponent
import com.example.hsexercise.common.di.DaggerAppComponent
import com.example.hsexercise.common.di.modules.ApplicationModule

class App: Application() {

//    Dagger component - for sake of simplicity it's one component without scopes, since app is very basic
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .applicationModule(ApplicationModule(applicationContext))
            .build()
    }
}