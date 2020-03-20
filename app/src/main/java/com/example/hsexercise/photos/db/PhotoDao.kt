package com.example.hsexercise.photos.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.hsexercise.photos.model.Photo

@Dao
interface PhotoDao {
    @Query("SELECT * FROM photos")
    fun getAllPhotos(): LiveData<List<Photo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(photos: List<Photo>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(photo: Photo)

    @Query("DELETE FROM photos")
    fun deleteAll();
}
