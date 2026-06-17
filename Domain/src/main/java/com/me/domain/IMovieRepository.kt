package com.me.domain

import kotlinx.coroutines.flow.Flow

interface IMovieRepository {
    fun loadMovies(): Flow<UiState<List<Movie>>>
    fun updateMovie(id: Int, check: Boolean)
}
