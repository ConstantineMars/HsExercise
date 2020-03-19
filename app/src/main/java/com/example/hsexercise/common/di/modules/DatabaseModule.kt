package com.example.hsexercise.common.di.modules

import android.content.Context
import androidx.room.Room
import com.example.hsexercise.common.database.PhotoDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

const val DATABASE_NAME = "headspace-database"

@Module
object DatabaseModule {

    @Provides @Singleton
    fun provideRoomDatabase(applicationContext: Context): PhotoDatabase {
        return Room.databaseBuilder(applicationContext, PhotoDatabase::class.java,
                DATABASE_NAME
            )
            .fallbackToDestructiveMigration()
            .build()
    }
}
