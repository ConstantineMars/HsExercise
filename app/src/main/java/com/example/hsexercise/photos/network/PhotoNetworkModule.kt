package com.example.hsexercise.photos.network

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
object PhotoNetworkModule {
    @Provides
    fun providePhotoService(retrofit: Retrofit): PhotoService = retrofit.create(PhotoService::class.java)
}