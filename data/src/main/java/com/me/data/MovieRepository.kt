package com.me.data

import com.me.data.datasource.local.dao.MovieDao
import com.me.data.datasource.local.entities.MovieEntity
import com.me.data.datasource.remote.MovieService
import com.me.data.datasource.remote.api.ApiResponse
import com.me.data.datasource.remote.response.MovieResponse
import com.me.data.di.utils.NetworkBoundResource
import com.me.domain.Constants
import com.me.domain.IMovieRepository
import com.me.domain.Movie
import com.me.domain.UiState
import com.me.domain.UiState.Error
import com.me.domain.UiState.Loading
import com.me.domain.UiState.Success
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

    override fun loadMovies(selectedPage: Int): Flow<UiState<List<Movie>>> {

        return object : NetworkBoundResource<List<MovieEntity>, MovieResponse>() {
            override suspend fun saveCallResult(response: MovieResponse) {
                response.movies
                val list = response.movies.map {
                    it.toMovieEntity()
                }
                dao.deleteMovies()
                dao.setMovies(list)
            }

            override fun shouldFetch(data: List<MovieEntity>?): Boolean = data.isNullOrEmpty()

            override fun loadFromDb(): Flow<List<MovieEntity>> = dao.getMovies()

            override suspend fun createCall(): ApiResponse<MovieResponse> =
                service.loadMovies(
                    Constants.API_KEY,
                    selectedPage
                ).first()

        }.asFlow().map { resource ->
            when (resource) {
                is Success -> Success(resource.data.map { it.toMovie() })

                is Error -> Error(
                    resource.message,
                    resource.data?.map { it.toMovie() }
                )

                is Loading -> Loading(resource.data?.map { it.toMovie() })
            }
        }
    }

    override fun updateMovie(id: Int, check: Boolean) = dao.updateMovie(id, check)

}
