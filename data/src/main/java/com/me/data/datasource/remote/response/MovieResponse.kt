package com.me.data.datasource.remote.response


import com.google.gson.annotations.SerializedName
import com.me.data.datasource.remote.model.MovieModel

data class MovieResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val movies: List<MovieModel>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)