package com.frogtest.movieguru.data.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.frogtest.movieguru.data.cache.MovieDatabase
import com.frogtest.movieguru.data.cache.entity.movie.MovieEntity
import com.frogtest.movieguru.data.mappers.toMovie
import com.frogtest.movieguru.data.mappers.toMovieDetails
import com.frogtest.movieguru.data.mappers.toMovieDetailsEntity
import com.frogtest.movieguru.data.network.MovieNetworkMediator
import com.frogtest.movieguru.data.network.api.OMDBMovieAPI
import com.frogtest.movieguru.data.network.api.TMDBMovieAPI
import com.frogtest.movieguru.domain.model.movie.Movie
import com.frogtest.movieguru.domain.model.movie_details.MovieDetails
import com.frogtest.movieguru.domain.repository.MovieRepository
import com.frogtest.movieguru.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    val omDBApi: OMDBMovieAPI,
    val tmDBApi: TMDBMovieAPI,
    val movieDatabase: MovieDatabase
) : MovieRepository {

    private val TAG = "MovieRepositoryImpl"

    @OptIn(ExperimentalPagingApi::class)
    override fun getMovies(sort: Boolean, query: String): Flow<PagingData<MovieEntity>> {
        val pagingSourceFactory = { movieDatabase.movieDao.getMovies() }
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = MovieNetworkMediator(
                movieApi = tmDBApi,
                movieDb = movieDatabase,
                sort = sort,
                query = query
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    override suspend fun getMovie(
        id: Int
    ): Flow<Resource<Movie>> {
        return flow {
            emit(Resource.Loading(true))

            Log.d(TAG, "id: $id")

            val cacheMovie = movieDatabase.movieDao.getMovie(id)

            if (cacheMovie == null) {
                emit(Resource.Error("Movie not found"))
                return@flow
            }
            emit(Resource.Success(cacheMovie.toMovie()))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun getMovieDetails(
        fetchFromNetwork: Boolean,
        id: Int,
        type: String
    ): Flow<Resource<MovieDetails>> {
        return flow {
            emit(Resource.Loading(true))

            Log.d(TAG, "imdbID: $id")

            val cacheMovieDetails = movieDatabase.movieDetailsDao.getMovieDetails(id)
            cacheMovieDetails?.let {
                emit(Resource.Success(cacheMovieDetails.toMovieDetails()))
            }


            val isNotInCache = cacheMovieDetails == null
            val shouldJustLoadFromCache = !fetchFromNetwork && !isNotInCache
            if (shouldJustLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }

            val networkMovieDetails = try {
                val response = tmDBApi.getMovieDetails(id = id, type = type)

                response

            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(e.message ?: "An error occurred"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(e.message ?: "An error occurred"))
                null
            }

            networkMovieDetails?.let {
                movieDatabase.movieDetailsDao.deleteMovieDetails(id)
                movieDatabase.movieDetailsDao.insertMovieDetails(it.toMovieDetailsEntity())
            }

            val data = movieDatabase.movieDetailsDao.getMovieDetails(id)
            if (data == null) {
                emit(Resource.Error("An error occurred"))
            } else {
                emit(Resource.Success(data.toMovieDetails()))
            }

            emit(Resource.Loading(false))
        }
    }


}