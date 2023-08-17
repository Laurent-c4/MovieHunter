package com.frogtest.movieguru.domain.repository

import androidx.paging.PagingData
import com.frogtest.movieguru.data.cache.entity.MovieEntity
import com.frogtest.movieguru.domain.model.MovieDetails
import com.frogtest.movieguru.domain.model.MovieVideo
import com.frogtest.movieguru.util.Resource
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun getMovies(): Flow<PagingData<MovieEntity>>

    suspend fun getMovieDetails(
        fetchFromNetwork: Boolean,
        imdbID: String
    ): Flow<Resource<MovieDetails>>

    suspend fun getMovieVideos(
        fetchFromNetwork: Boolean,
        tmdbID: Int
    ): Flow<Resource<List<MovieVideo>>>
}