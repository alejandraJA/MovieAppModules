package com.me.data

import com.me.data.datasource.local.entities.MovieEntity
import com.me.data.datasource.remote.model.MovieModel
import com.me.domain.Movie

fun MovieEntity.toMovie(): Movie = Movie(
    id,
    like,
    adult,
    backdropPath,
    genreIds,
    originalLanguage,
    originalTitle,
    overview,
    popularity,
    posterPath,
    releaseDate,
    title,
    video,
    voteAverage,
    voteCount,
)

fun MovieModel.toMovieEntity(): MovieEntity = MovieEntity(
    id = id,
    adult = adult,
    backdropPath = backdropPath,
    genreIds = genreIds,
    originalLanguage = originalLanguage,
    originalTitle = originalTitle,
    overview = overview,
    popularity = popularity,
    posterPath = posterPath,
    releaseDate = releaseDate,
    title = title,
    video = video,
    voteAverage = voteAverage,
    voteCount = voteCount,
    like = false,
)

