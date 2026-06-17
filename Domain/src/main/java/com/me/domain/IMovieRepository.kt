package com.me.domain

import androidx.lifecycle.LiveData

interface IMovieRepository {
    fun loadMovies(): LiveData<Resource<List<Movie>>>
    fun updateMovie(id: Int, check: Boolean)
}
