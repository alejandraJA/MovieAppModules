package com.me.data.remote

import androidx.lifecycle.LiveData
import com.me.data.remote.api.ApiResponse
import com.me.data.remote.response.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {
    @GET("discover/movie")
    fun loadMovies(
        @Query("api_key") apiKey: String
    ): LiveData<ApiResponse<MoviesResponse>>
}