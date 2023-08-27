package com.c4entertainment.moviehunter.data.network

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.c4entertainment.moviehunter.data.cache.MovieDatabase
import com.c4entertainment.moviehunter.data.cache.entity.search.MovieSearchEntity
import com.c4entertainment.moviehunter.data.cache.entity.search.MovieSearchRemoteKeyEntity
import com.c4entertainment.moviehunter.data.mappers.toMovieSearchEntity
import com.c4entertainment.moviehunter.data.network.api.TMDBMovieAPI

@OptIn(ExperimentalPagingApi::class)
class SearchMovieNetworkMediator(
    private val movieApi: TMDBMovieAPI,
    private val movieDb: MovieDatabase,
    private val sort: Boolean = false,
    private val type: String = "movie",
    private val query: String
): RemoteMediator<Int, MovieSearchEntity>() {

    private val movieDao = movieDb.movieSearchDao
    private val remoteKeyDao = movieDb.movieSearchRemoteKeyDao

    private val TAG = "SearchMovieNetworkMediator"
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieSearchEntity>
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
//                    Log.d(TAG, "load: nextPage: $nextPage")
                    nextPage

                }
            }

            Log.d(TAG, "load: type: $type")

            val response = movieApi.searchMovies(type = type, page = currentPage, query = query).results?: emptyList()

            val endOfPaginationReached = response.isEmpty()

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            movieDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    movieDao.clearAll()
                    remoteKeyDao.deleteAllRemoteKeys()
                }

                val keys = response.map { movieDto ->
                    MovieSearchRemoteKeyEntity(
                        id = movieDto.id,
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }

                remoteKeyDao.addAllRemoteKeys(remoteKeys = keys)
                movieDao.insertAll(movies = response.map { it.toMovieSearchEntity() })
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, MovieSearchEntity>
    ): MovieSearchRemoteKeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                remoteKeyDao.getRemoteKey(id = id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, MovieSearchEntity>
    ): MovieSearchRemoteKeyEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { movieEntity ->
                remoteKeyDao.getRemoteKey(id = movieEntity.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, MovieSearchEntity>
    ): MovieSearchRemoteKeyEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { movieEntity ->
                remoteKeyDao.getRemoteKey(id = movieEntity.id)
            }
    }

}