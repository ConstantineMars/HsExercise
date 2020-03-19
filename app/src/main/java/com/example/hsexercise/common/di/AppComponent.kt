package com.example.hsexercise.common.di

import com.example.hsexercise.common.di.modules.ApplicationModule
import com.example.hsexercise.common.di.modules.DatabaseModule
import com.example.hsexercise.common.di.modules.NetworkModule
import com.example.hsexercise.photos.FeatureActivity
import com.example.hsexercise.photos.network.PhotoNetworkModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, DatabaseModule::class, PhotoNetworkModule::class, NetworkModule::class])
interface AppComponent {
    fun injectFeatureActivity(activity: FeatureActivity)
}