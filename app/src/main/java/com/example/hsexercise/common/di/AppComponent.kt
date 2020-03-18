package com.example.hsexercise.common.di

import com.example.hsexercise.feature.FeatureActivity
import com.example.hsexercise.feature.network.PhotoNetworkModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [PhotoNetworkModule::class])
interface AppComponent {
    fun injectFeatureActivity(activity: FeatureActivity)
}