package com.me.data.di

import android.content.Context
import com.me.data.datasource.local.dao.MovieDao
import com.me.data.datasource.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext app: Context): AppDatabase =
        AppDatabase.getInstance(app)

    @Singleton
    @Provides
    fun provideMovieDao(db: AppDatabase): MovieDao = db.movieDao()
}