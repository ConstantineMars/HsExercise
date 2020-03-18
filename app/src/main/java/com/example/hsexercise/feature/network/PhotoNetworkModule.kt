package com.example.hsexercise.feature.network

import com.example.hsexercise.common.NetworkProvider
import dagger.Module
import dagger.Provides

@Module
object PhotoNetworkModule {
    @Provides
    fun providePhotoService(): PhotoService = NetworkProvider.provideRestClient().createRetrofitAdapter().create(PhotoService::class.java)
}