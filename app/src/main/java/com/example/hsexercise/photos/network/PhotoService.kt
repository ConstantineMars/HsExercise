package com.example.hsexercise.photos.network

import com.example.hsexercise.photos.model.Photo
import io.reactivex.Flowable
import retrofit2.http.GET

interface PhotoService {
    @GET("/v2/list")
    fun listRepos(): Flowable<List<Photo>>
}