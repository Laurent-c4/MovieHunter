package com.frogtest.movieguru.data.network

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.frogtest.movieguru.data.cache.MovieDatabase
import com.frogtest.movieguru.data.cache.entity.movie.MovieEntity
import com.frogtest.movieguru.data.cache.entity.movie.MovieRemoteKeyEntity
import com.frogtest.movieguru.data.mappers.toMovieEntity
import com.frogtest.movieguru.data.network.api.TMDBMovieAPI


private const val TAG = "MovieNetworkMediator"

@OptIn(ExperimentalPagingApi::class)
class MovieNetworkMediator(
    private val movieApi: TMDBMovieAPI,
    private val movieDb: MovieDatabase,
    private val sort: Boolean = false,
    private val type: String = "movie"
): RemoteMediator<Int, MovieEntity>() {

    private val movieDao = movieDb.movieDao
    private val remoteKeyDao = movieDb.movieRemoteKeyDao
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    prevPage
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                        Log.d(TAG, "load: nextPage: $nextPage")
                    nextPage

                }
            }

            val response = movieApi.getTrendingMovies(type = type,page = currentPage).results?: emptyList()

            val endOfPaginationReached = response.isEmpty()

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            movieDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    movieDao.clearAll()
                    remoteKeyDao.deleteAllRemoteKeys()
                }

                val keys = response.map { movieDto ->
                    MovieRemoteKeyEntity(
                        id = movieDto.id,
                        prevPage = prevPage,
                        nextPage = nextPage
                    ) }

                remoteKeyDao.addAllRemoteKeys(remoteKeys = keys)
                movieDao.insertAll(movies = response.map { it.toMovieEntity() })
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, MovieEntity>
    ): MovieRemoteKeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                remoteKeyDao.getRemoteKey(id = id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, MovieEntity>
    ): MovieRemoteKeyEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { movieEntity ->
                remoteKeyDao.getRemoteKey(id = movieEntity.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, MovieEntity>
    ): MovieRemoteKeyEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { movieEntity ->
                remoteKeyDao.getRemoteKey(id = movieEntity.id)
            }
    }

}