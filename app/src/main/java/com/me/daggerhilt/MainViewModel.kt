package com.me.daggerhilt

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.me.domain.IMovieRepository
import com.me.domain.Movie
import com.me.domain.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: IMovieRepository
) : ViewModel() {
    val movies: Flow<Resource<List<Movie>>> = repository.loadMovies()
    fun updateMovie(id: Int, check: Boolean) = repository.updateMovie(id, check)
}