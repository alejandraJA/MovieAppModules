package com.me.domain

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

interface IMovieRepository {
    fun loadMovies(): Flow<Resource<List<Movie>>>
    fun updateMovie(id: Int, check: Boolean)
}
