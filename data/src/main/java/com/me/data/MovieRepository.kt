package com.me.data

import com.me.data.datasource.local.dao.MovieDao
import com.me.data.datasource.remote.MovieService
import com.me.data.datasource.remote.api.ApiResponse
import com.me.data.datasource.remote.response.MovieResponse
import com.me.data.di.utils.NetworkBoundResource
import com.me.domain.Constants
import com.me.domain.IMovieRepository
import com.me.domain.MovieUiState
import com.me.domain.UiState
import com.me.domain.UiState.Error
import com.me.domain.UiState.Loading
import com.me.domain.UiState.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val dao: MovieDao,
    private val service: MovieService
) : IMovieRepository {

    override fun loadMovies(selectedPage: Int): Flow<UiState<MovieUiState>> {

        return object : NetworkBoundResource<MovieUiState, MovieResponse>() {
            override suspend fun saveCallResult(response: MovieResponse) {
                response.movies
                val list = response.movies.map {
                    it.toMovieEntity()
                }
                dao.deleteMovies()
                dao.setMovies(list)
            }

            override fun shouldFetch(data: MovieUiState?): Boolean = data != null

            override fun loadFromDb(): Flow<MovieUiState> = flow {
                emit(
                    MovieUiState(
                        currentPage = 1,
                        totalPages = 1,
                        dao.getMovies().map { it.toMovie() }
                    )
                )
            }

            override suspend fun createCall(): ApiResponse<MovieResponse> =
                service.loadMovies(
                    Constants.API_KEY,
                    selectedPage
                ).first()

        }.asFlow().map { resource ->


            when (resource) {
                is Success ->
                    Success(
                        MovieUiState(
                            currentPage = resource.data.currentPage,
                            totalPages = resource.data.totalPages,
                            movies = resource.data.movies
                        )
                    )

                is Error -> Error(
                    resource.message,
                    MovieUiState(
                        currentPage = resource.data?.currentPage ?: 1,
                        totalPages = resource.data?.totalPages ?: 1,
                        movies = resource.data?.movies ?: listOf()
                    )
                )

                is Loading -> Loading(
                    MovieUiState(
                        currentPage = resource.data?.currentPage ?: 1,
                        totalPages = resource.data?.totalPages ?: 1,
                        movies = resource.data?.movies ?: listOf()
                    )
                )
            }
        }
    }

    override fun updateMovie(id: Int, check: Boolean) = dao.updateMovie(id, check)

}