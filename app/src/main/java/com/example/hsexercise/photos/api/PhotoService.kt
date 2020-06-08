package com.example.hsexercise.photos.api

import com.example.hsexercise.photos.model.Photo
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Service for getting photos list
 * Keep original Retrofit naming ("Service") - though, it might be named "NetworkDatasource" in AAC terminology
 */
interface PhotoService {
    @GET("/v2/list")
    fun listPhotos(): Flowable<List<Photo>>

    @GET("/v2/list")
    fun listPhotos(@Query("page") page: Int, @Query("limit") limit: Int): Flowable<List<Photo>>
}