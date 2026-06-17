package com.me.daggerhilt.di

import com.me.data.MovieRepository
import com.me.data.UserRepository
import com.me.domain.IMovieRepository
import com.me.domain.IUserRepository
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
    fun provideMovieRepository(movieRepository: MovieRepository): IMovieRepository =
        movieRepository

    @Singleton
    @Provides
    fun provideUserRepository(userRepository: UserRepository): IUserRepository =
        userRepository
}