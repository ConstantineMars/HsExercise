package com.example.hsexercise.feature.network

import com.example.hsexercise.common.NetworkProvider

object PhotoNetworkProvider {
    fun providePhotoService(): PhotoService = NetworkProvider.provideRestClient().createRetrofitAdapter().create(PhotoService::class.java)
}