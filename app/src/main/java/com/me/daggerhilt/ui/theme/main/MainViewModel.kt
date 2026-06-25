package com.me.daggerhilt.ui.theme.main

import androidx.lifecycle.ViewModel
import com.me.domain.IMovieRepository
import com.me.domain.Movie
import com.me.domain.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: IMovieRepository
) : ViewModel() {
    val movies: Flow<UiState<List<Movie>>> = repository.loadMovies(1)
    fun updateMovie(id: Int, check: Boolean) = repository.updateMovie(id, check)
}