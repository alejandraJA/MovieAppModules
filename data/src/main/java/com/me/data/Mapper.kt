package com.me.data

import com.me.data.local.entities.MovieEntity
import com.me.domain.Movie

fun MovieEntity.toMovie(): Movie =
    Movie(id, title, originalTitle, overview, posterPath, like)