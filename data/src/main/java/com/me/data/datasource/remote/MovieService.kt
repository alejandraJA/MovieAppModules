package com.me.data.datasource.remote

import com.me.data.datasource.remote.api.ApiResponse
import com.me.data.datasource.remote.response.MovieResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {
    @GET("discover/movie")
    fun loadMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): Flow<ApiResponse<MovieResponse>>
}