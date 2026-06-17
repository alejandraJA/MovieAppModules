package com.me.daggerhilt.di

import com.me.data.MovieRepository
import com.me.domain.IMovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideMovieRepository(movieRepository: MovieRepository): IMovieRepository = movieRepository
}