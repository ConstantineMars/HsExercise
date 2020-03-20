package com.example.hsexercise.photos.api

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
object PhotoNetworkModule {
    @Provides
    fun providePhotoService(retrofit: Retrofit): PhotoService = retrofit.create(PhotoService::class.java)
}