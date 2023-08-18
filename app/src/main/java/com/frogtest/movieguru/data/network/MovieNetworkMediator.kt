package com.frogtest.movieguru.data.network

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.frogtest.movieguru.data.cache.MovieDatabase
import com.frogtest.movieguru.data.cache.entity.MovieEntity
import com.frogtest.movieguru.data.cache.entity.MovieRemoteKeyEntity
import com.frogtest.movieguru.data.mappers.toMovieEntity
import com.frogtest.movieguru.data.network.api.OMDBMovieAPI
import com.frogtest.movieguru.data.network.dto.MovieDto
import javax.inject.Inject

private const val TAG = "MovieNetworkMediator"

@OptIn(ExperimentalPagingApi::class)
class MovieNetworkMediator @Inject constructor(
    private val OMDBMovieApi: OMDBMovieAPI,
    private val movieDb: MovieDatabase,
    private val sort: Boolean = false
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

            val response = OMDBMovieApi.getMovies(page = currentPage).data

            val endOfPaginationReached = response.isEmpty()

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            movieDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    movieDao.clearAll()
                    remoteKeyDao.deleteAllRemoteKeys()
                }

                val keys =  if (sort) {
                   dirtyFix(response)
                } else
                    response.map { movieDto ->
                    MovieRemoteKeyEntity(
                        id = movieDto.imdbID,
                        year = movieDto.year,
                        prevPage = prevPage,
                        nextPage = nextPage
                    ) }


                remoteKeyDao.deleteAllRemoteKeys()
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
            state.closestItemToPosition(position)?.imdbID?.let { id ->
                remoteKeyDao.getRemoteKey(id = id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, MovieEntity>
    ): MovieRemoteKeyEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { movieEntity ->
                remoteKeyDao.getRemoteKey(id = movieEntity.imdbID)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, MovieEntity>
    ): MovieRemoteKeyEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { movieEntity ->
                remoteKeyDao.getRemoteKey(id = movieEntity.imdbID)
            }
    }

    private suspend fun dirtyFix(response: List<MovieDto>): List<MovieRemoteKeyEntity>{
        //get all keys from DB
        val keysdb = remoteKeyDao.getAllRemoteKeys()

        //append keys from API

        val keysAPI = response.map { movieDto ->
            MovieRemoteKeyEntity(
                id = movieDto.imdbID,
                year = movieDto.year,
                prevPage = null,
                nextPage = null
            )
        }

        //append keys from API to DB

        val newKeys = keysdb.toMutableList()

        newKeys.addAll(keysAPI)

        //sort keys

        val finalKeys = newKeys.sortedBy { it.year }

        //modify prev and next page using pagination 10
       return  finalKeys.mapIndexed { index, movieRemoteKeyEntity ->
            MovieRemoteKeyEntity(
                id = movieRemoteKeyEntity.id,
                prevPage = if(index / 10 == 0) null else index / 10,
                nextPage =  index /10 + 2,
                year = movieRemoteKeyEntity.year
            )
        }



    }
}