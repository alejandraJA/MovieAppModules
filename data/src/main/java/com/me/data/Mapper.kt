package com.me.data

import com.me.data.datasource.local.entities.MovieEntity
import com.me.domain.Movie

fun MovieEntity.toMovie(): Movie =
    Movie(id, title, originalTitle, overview, posterPath, like)