package com.me.data.remote.response

import com.google.gson.annotations.SerializedName
import com.me.data.remote.model.MovieModel

data class MoviesResponse(
    @SerializedName("results")
    val listMovies: List<MovieModel>,

    @SerializedName("total_results")
    val totalResults: Int
)