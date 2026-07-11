package com.me.domain

data class Movie(
    val id: Int,
    val like: Boolean,
    val adult: Boolean,
    val backdropPath: String,
    val genreIds: List<Int>,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int,
)

data class MovieUiState(
    val currentPage: Int = 1,
    val totalPages: Int = 1,
    val movies: List<Movie>
)