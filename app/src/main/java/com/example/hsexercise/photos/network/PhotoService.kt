package com.example.hsexercise.photos.network

import com.example.hsexercise.photos.model.Photo
import io.reactivex.Flowable
import retrofit2.http.GET

/**
 * Service for getting photos list
 * Keep original Retrofit naming ("Service") - though, it might be named "NetworkDatasource" in AAC terminology
 */
interface PhotoService {
    @GET("/v2/list")
    fun listRepos(): Flowable<List<Photo>>
}