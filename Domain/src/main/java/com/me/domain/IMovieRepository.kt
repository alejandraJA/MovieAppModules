package com.me.domain

import kotlinx.coroutines.flow.Flow

interface IMovieRepository {
    fun loadMovies(selectedPage: Int): Flow<UiState<List<Movie>>>
    fun updateMovie(id: Int, check: Boolean)
}
