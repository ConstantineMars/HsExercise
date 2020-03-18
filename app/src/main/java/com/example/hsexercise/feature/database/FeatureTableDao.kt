package com.example.hsexercise.feature.database

import androidx.room.*
import io.reactivex.Maybe

@Dao
interface FeatureTableDao {
    @Query("SELECT * FROM feature")
    fun getAll(): Maybe<List<FeatureModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(models: List<FeatureModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(featureModel: FeatureModel)
}

@Entity(tableName = "feature")
data class FeatureModel(
    @PrimaryKey
    val id: String,
    val author: String,
    val url: String,
    val width: Int,
    val height: Int
)
