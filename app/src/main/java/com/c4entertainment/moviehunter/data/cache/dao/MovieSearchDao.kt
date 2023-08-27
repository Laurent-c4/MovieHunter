package com.c4entertainment.moviehunter.data.cache.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.c4entertainment.moviehunter.data.cache.entity.search.MovieSearchEntity

@Dao
interface MovieSearchDao {

    @Upsert
    suspend fun insertAll(movies: List<MovieSearchEntity>)


    @Query("SELECT * FROM movies_search")
    fun getMovies(): PagingSource<Int, MovieSearchEntity>

    @Query("DELETE FROM movies_search")
    suspend fun clearAll()
}