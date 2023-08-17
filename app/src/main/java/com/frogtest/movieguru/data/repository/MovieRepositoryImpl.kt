package com.frogtest.movieguru.data.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.frogtest.movieguru.data.cache.MovieDatabase
import com.frogtest.movieguru.data.cache.entity.MovieEntity
import com.frogtest.movieguru.data.mappers.toMovieDetails
import com.frogtest.movieguru.data.mappers.toMovieDetailsEntity
import com.frogtest.movieguru.data.mappers.toMovieVideo
import com.frogtest.movieguru.data.mappers.toMovieVideoEntity
import com.frogtest.movieguru.data.network.MovieNetworkMediator
import com.frogtest.movieguru.data.network.api.OMDBMovieAPI
import com.frogtest.movieguru.data.network.api.TMDBMovieAPI
import com.frogtest.movieguru.domain.model.Movie
import com.frogtest.movieguru.domain.model.MovieDetails
import com.frogtest.movieguru.domain.model.MovieVideo
import com.frogtest.movieguru.domain.repository.MovieRepository
import com.frogtest.movieguru.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton class MovieRepositoryImpl @Inject constructor(
    val omDBApi: OMDBMovieAPI,
    val tmDBApi: TMDBMovieAPI,
    val movieDatabase: MovieDatabase
): MovieRepository {

    private val TAG = "MovieRepositoryImpl"

    @OptIn(ExperimentalPagingApi::class)
    override fun getMovies(): Flow<PagingData<MovieEntity>> {
        val pagingSourceFactory = { movieDatabase.movieDao.pagingSource() }
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = MovieNetworkMediator(
                OMDBMovieApi = omDBApi,
                movieDb = movieDatabase
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    override suspend fun getMovieDetails(
        fetchFromNetwork: Boolean,
        imdbID: String
    ): Flow<Resource<MovieDetails>> {
        return flow {
            emit(Resource.Loading(true))

            Log.d(TAG, "imdbID: $imdbID")

            val cacheMovieDetails = movieDatabase.movieDetailsDao.getMovieDetails(imdbID)
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
                val response = omDBApi.getMovieDetails(imdbID = imdbID)

                // TMDBID will be used to get the movie videos
                try {
                val tmDBDetails = tmDBApi.getMovieDetails(externalID = imdbID)

                if (tmDBDetails.movieResults.isNotEmpty()){
                    Log.d(TAG, "getMovieDetails: ${tmDBDetails.movieResults[0].id}")
                    response.tmdbID = tmDBDetails.movieResults[0].id
                }
                }
                catch(e: Exception){
                    e.printStackTrace()
                    response.tmdbID = null
                }

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
                movieDatabase.movieDetailsDao.deleteMovieDetails(imdbID)
                movieDatabase.movieDetailsDao.insertMovieDetails(it.toMovieDetailsEntity())
            }

            val data = movieDatabase.movieDetailsDao.getMovieDetails(imdbID)
            if (data == null) {
                emit(Resource.Error("An error occurred"))
            } else {
                emit(Resource.Success(data.toMovieDetails()))
            }

            emit(Resource.Loading(false))
        }
    }

    override suspend fun getMovieVideos(
        fetchFromNetwork: Boolean,
        tmdbID: Int
    ): Flow<Resource<List<MovieVideo>>> {
        return flow {
            emit(Resource.Loading(true))

            val cacheMovieVideos = movieDatabase.movieVideoDao.getMovieVideos(tmdbID)
            emit(Resource.Success(cacheMovieVideos.map { it.toMovieVideo() }))


            val isNotInCache = cacheMovieVideos.isEmpty()
            val shouldJustLoadFromCache = !fetchFromNetwork && !isNotInCache
            if (shouldJustLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }

            val networkMovieVideos = try {
                tmDBApi.getMovieVideos(tmdbID = tmdbID)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(e.message ?: "An error occurred"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(e.message ?: "An error occurred"))
                null
            }

            networkMovieVideos?.let {
                val movieVideos = it.results.map { movieVideo ->
                    movieVideo.tmdbID = tmdbID
                    movieVideo.toMovieVideoEntity()
                }

                movieDatabase.movieVideoDao.deleteMovieVideos(tmdbID)
                movieDatabase.movieVideoDao.insertAll(movieVideos)
            }

            val data = movieDatabase.movieVideoDao.getMovieVideos(tmdbID).map { it.toMovieVideo() }
            if (data.isEmpty()) {
                emit(Resource.Error("No trailers available"))
            } else {
                emit(Resource.Success(data))
            }

            emit(Resource.Loading(false))
        }
    }


}