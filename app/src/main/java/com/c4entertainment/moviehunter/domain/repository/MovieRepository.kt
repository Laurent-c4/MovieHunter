package com.c4entertainment.moviehunter.domain.repository

import androidx.paging.PagingData
import com.c4entertainment.moviehunter.data.cache.entity.movie.MovieEntity
import com.c4entertainment.moviehunter.data.cache.entity.search.MovieSearchEntity
import com.c4entertainment.moviehunter.domain.model.movie_details.MovieDetails
import com.c4entertainment.moviehunter.domain.model.movie.Movie
import com.c4entertainment.moviehunter.util.Resource
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun getMovies(sort: Boolean = false, type: String): Flow<PagingData<MovieEntity>>

    fun searchMovies(sort: Boolean = false, type:String, query: String): Flow<PagingData<MovieSearchEntity>>

    suspend fun getMovie(
        id: Int,
        type: String
    ): Flow<Resource<Movie>>

    suspend fun getMovieDetails(
        fetchFromNetwork: Boolean,
        id: Int,
        type: String
    ): Flow<Resource<MovieDetails>>

suspend fun clearSearch(): Flow<Resource<Boolean>>

}