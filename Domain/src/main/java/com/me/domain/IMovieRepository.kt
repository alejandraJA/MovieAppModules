package com.me.domain

import kotlinx.coroutines.flow.Flow

interface IMovieRepository {
    fun loadMovies(selectedPage: Int): Flow<UiState<MovieUiState>>
    fun updateMovie(id: Int, check: Boolean)
}
