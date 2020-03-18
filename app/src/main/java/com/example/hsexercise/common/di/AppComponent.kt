package com.example.hsexercise.common.di

import com.example.hsexercise.common.NetworkModule
import com.example.hsexercise.feature.FeatureActivity
import com.example.hsexercise.feature.network.PhotoNetworkModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [PhotoNetworkModule::class, NetworkModule::class])
interface AppComponent {
    fun injectFeatureActivity(activity: FeatureActivity)
}