package com.me.daggerhilt.ui.theme.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.me.domain.IMovieRepository
import com.me.domain.MovieUiState
import com.me.domain.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: IMovieRepository
) : ViewModel() {
    val movies: Flow<UiState<MovieUiState>> = repository.loadMovies(1)
    fun updateMovie(id: Int, check: Boolean) = repository.updateMovie(id, check)

    fun changePage(page: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.loadMovies(page)
    }
}