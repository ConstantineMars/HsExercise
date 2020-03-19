package com.example.hsexercise.common.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val applicationContext: Context) {
    @Provides
    fun provideApplicationContext(): Context = applicationContext
}