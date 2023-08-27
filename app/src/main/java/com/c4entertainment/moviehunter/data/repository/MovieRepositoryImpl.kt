package com.c4entertainment.moviehunter.data.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.c4entertainment.moviehunter.data.cache.MovieDatabase
import com.c4entertainment.moviehunter.data.cache.entity.movie.MovieEntity
import com.c4entertainment.moviehunter.data.cache.entity.search.MovieSearchEntity
import com.c4entertainment.moviehunter.data.mappers.toMovie
import com.c4entertainment.moviehunter.data.mappers.toMovieDetails
import com.c4entertainment.moviehunter.data.mappers.toMovieDetailsEntity
import com.c4entertainment.moviehunter.data.network.MovieNetworkMediator
import com.c4entertainment.moviehunter.data.network.SearchMovieNetworkMediator
import com.c4entertainment.moviehunter.data.network.api.TMDBMovieAPI
import com.c4entertainment.moviehunter.domain.model.movie.Movie
import com.c4entertainment.moviehunter.domain.model.movie_details.MovieDetails
import com.c4entertainment.moviehunter.domain.repository.MovieRepository
import com.c4entertainment.moviehunter.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    val tmDBApi: TMDBMovieAPI,
    val movieDatabase: MovieDatabase
) : MovieRepository {

    private val TAG = "MovieRepositoryImpl"

    private val movieDao = movieDatabase.movieDao
    private val movieDetailsDao = movieDatabase.movieDetailsDao
    private val movieSearchDao = movieDatabase.movieSearchDao

    @OptIn(ExperimentalPagingApi::class)
    override fun getMovies(sort: Boolean, type: String): Flow<PagingData<MovieEntity>> {
        val pagingSourceFactory = { movieDao.getMovies() }
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = MovieNetworkMediator(
                movieApi = tmDBApi,
                movieDb = movieDatabase,
                type = type,
                sort = sort,

            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun searchMovies(sort: Boolean, type:String, query: String): Flow<PagingData<MovieSearchEntity>> {
        val pagingSourceFactory = { movieSearchDao.getMovies() }
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = SearchMovieNetworkMediator(
                movieApi = tmDBApi,
                movieDb = movieDatabase,
                query = query,
                type = type,
                sort = sort,

            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    override suspend fun getMovie(
        id: Int,
        type: String
    ): Flow<Resource<Movie>> {
        return flow {
            emit(Resource.Loading(true))

            Log.d(TAG, "id: $id")

            val cacheMovie = movieDao.getMovie(id)

//            if (cacheMovie == null) {
//                emit(Resource.Error("Movie not found"))
//                return@flow
//            }

            cacheMovie?.let {
                emit(Resource.Success(cacheMovie.toMovie()))
                emit(Resource.Loading(false))
                return@flow
            }

            val networkMovie = try {
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

            val movie = Movie(
                adult = networkMovie?.adult ?: false,
                backdropPath = networkMovie?.backdropPath ?: "",
                genreIds = networkMovie?.genres?.map { it.id!! } ?: listOf(),
                id = networkMovie?.id ?: 0,
                originalLanguage = networkMovie?.originalLanguage ?: "",
                originalTitle = networkMovie?.originalTitle ?: "",
                overview = networkMovie?.overview ?: "",
                popularity = networkMovie?.popularity ?: 0.0,
                posterPath = networkMovie?.posterPath ?: "",
                releaseDate = networkMovie?.releaseDate ?: networkMovie?.firstAirDate ?: "",
                title = networkMovie?.title ?: networkMovie?.name ?: "",
                video = networkMovie?.video ?: false,
                voteAverage = networkMovie?.voteAverage ?: 0.0,
                voteCount = networkMovie?.voteCount ?: 0,
                mediaType = networkMovie?.releaseDate?.let { "movie" }
                    ?: networkMovie?.firstAirDate?.let { "tv" } ?: ""

            )

            emit(Resource.Success(movie))


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

            val cacheMovieDetails = movieDetailsDao.getMovieDetails(id)
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
                movieDetailsDao.deleteMovieDetails(id)
                movieDetailsDao.insertMovieDetails(it.toMovieDetailsEntity())
            }

            val data = movieDetailsDao.getMovieDetails(id)
            if (data == null) {
                emit(Resource.Error("An error occurred"))
            } else {
                emit(Resource.Success(data.toMovieDetails()))
            }

            emit(Resource.Loading(false))
        }
    }


    override suspend fun clearSearch(): Flow<Resource<Boolean>> {
        return flow {
            movieSearchDao.clearAll()
            emit(Resource.Success(true))
        }
    }
}