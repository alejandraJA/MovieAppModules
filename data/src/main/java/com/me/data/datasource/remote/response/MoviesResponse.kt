package com.me.data.datasource.remote.response

import com.google.gson.annotations.SerializedName
import com.me.data.datasource.remote.model.MovieModel

data class MoviesResponse(
    @SerializedName("results")
    val listMovies: List<MovieModel>,

    @SerializedName("total_results")
    val totalResults: Int
)