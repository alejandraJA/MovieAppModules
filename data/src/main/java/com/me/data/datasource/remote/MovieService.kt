package com.me.data.datasource.remote

import com.me.data.datasource.remote.api.ApiResponse
import com.me.data.datasource.remote.response.MoviesResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {
    @GET("discover/movie")
    fun loadMovies(
        @Query("api_key") apiKey: String
    ): Flow<ApiResponse<MoviesResponse>>
}