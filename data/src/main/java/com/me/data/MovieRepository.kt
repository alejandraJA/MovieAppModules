package com.me.data

import com.me.data.datasource.local.dao.MovieDao
import com.me.data.di.utils.NetworkBoundResource
import com.me.data.datasource.local.entities.MovieEntity
import com.me.data.datasource.remote.MovieService
import com.me.data.datasource.remote.api.ApiResponse
import com.me.data.datasource.remote.response.MoviesResponse
import com.me.domain.IMovieRepository
import com.me.domain.Movie
import com.me.domain.Constants
import com.me.domain.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val dao: MovieDao,
    private val service: MovieService
) : IMovieRepository {
    override fun loadMovies(): Flow<UiState<List<Movie>>> =
        object : NetworkBoundResource<List<MovieEntity>, MoviesResponse>() {
            override suspend fun saveCallResult(response: MoviesResponse) {
                val list = response.listMovies.map {
                    MovieEntity(
                        it.id,
                        it.title,
                        it.originalTitle,
                        it.overview,
                        it.posterPath,
                        like = false
                    )
                }
                dao.deleteMovies()
                dao.setMovies(list)
            }

            override fun shouldFetch(data: List<MovieEntity>?): Boolean = data.isNullOrEmpty()

            override fun loadFromDb(): Flow<List<MovieEntity>> = dao.getMovies()

            override suspend fun createCall(): ApiResponse<MoviesResponse> =
                service.loadMovies(Constants.API_KEY).first()
        }.asFlow().map { resource ->
            when (resource) {
                is UiState.Success ->
                    UiState.Success(resource.data.map { it.toMovie() })

                is UiState.Error ->
                    UiState.Error(resource.message, resource.data?.map { it.toMovie() })

                is UiState.Loading -> UiState.Loading(resource.data?.map { it.toMovie() })
            }
        }

    override fun updateMovie(id: Int, check: Boolean) = dao.updateMovie(id, check)

}


