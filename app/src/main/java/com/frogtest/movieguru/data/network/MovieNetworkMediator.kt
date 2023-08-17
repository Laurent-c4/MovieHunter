package com.frogtest.movieguru.data.network

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.frogtest.movieguru.data.cache.MovieDatabase
import com.frogtest.movieguru.data.cache.entity.MovieEntity
import com.frogtest.movieguru.data.mappers.toMovieEntity
import com.frogtest.movieguru.data.network.api.OMDBMovieAPI
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

private const val TAG = "MovieNetworkMediator"

@OptIn(ExperimentalPagingApi::class)
class MovieNetworkMediator(
    private val OMDBMovieApi: OMDBMovieAPI,
    private val movieDb: MovieDatabase
): RemoteMediator<Int, MovieEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    Log.d(TAG, "loading... $loadType")
                    Log.d(TAG, "${state.config.pageSize}")
                    val lastItem = state.lastItemOrNull()
                    Log.d(TAG, "lastItem: $lastItem")
//                    if (lastItem == null) {
//                        1
//                    } else {
                        (movieDb.movieDao.getCount()/ state.config.pageSize) + 1
//                    }
                }
            }

//            delay(2000L)
            val call = OMDBMovieApi.getMovies(page = loadKey)
            val movies = call.data
            Log.d(TAG, "load: ${movies.size} items loaded, in page $loadKey")
            movieDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    movieDb.movieDao.clearAll()
                }
                movieDb.movieDao.insertAll(movies.map { it.toMovieEntity() })
            }

            MediatorResult.Success(endOfPaginationReached = movies.isEmpty())
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}