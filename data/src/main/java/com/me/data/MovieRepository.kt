package com.me.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.me.data.local.dao.MovieDao
import com.me.data.local.di.utils.AppExecutors
import com.me.data.local.di.utils.NetworkBoundResource
import com.me.data.local.entities.MovieEntity
import com.me.data.remote.MovieService
import com.me.data.remote.api.ApiResponse
import com.me.data.remote.response.MoviesResponse
import com.me.domain.IMovieRepository
import com.me.domain.Movie
import com.me.domain.Resource
import com.me.domain.Constants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val dao: MovieDao,
    private val service: MovieService,
    private val appExecutor: AppExecutors
) : IMovieRepository {
    override fun loadMovies(): LiveData<Resource<List<Movie>>> =
        object : NetworkBoundResource<List<MovieEntity>, MoviesResponse>(appExecutor) {
            override fun saveCallResult(response: MoviesResponse) =
                response.listMovies.forEach { movie ->
                    dao.setMovie(
                        MovieEntity(
                            movie.id,
                            movie.title,
                            movie.originalTitle,
                            movie.overview,
                            movie.posterPath,
                            like = false
                        )
                    )
                }

            override fun shouldFetch(data: List<MovieEntity>?): Boolean = data.isNullOrEmpty()

            override fun loadFromDb(): LiveData<List<MovieEntity>> = dao.getMovies()

            override fun createCall(): LiveData<ApiResponse<MoviesResponse>> =
                service.loadMovies(Constants.API_KEY)
        }.asLiveData().map { resource ->
            Resource(
                resource.status,
                resource.data?.map { it.toMovie() },
                resource.message
            )
        }

    override fun updateMovie(id: Int, check: Boolean) = dao.updateMovie(id, check)

}

private fun MovieEntity.toMovie(): Movie =
    Movie(id, title, originalTitle, overview, posterPath, like)


