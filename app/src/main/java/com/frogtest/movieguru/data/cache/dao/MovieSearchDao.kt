package com.frogtest.movieguru.data.cache.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.frogtest.movieguru.data.cache.entity.movie.MovieEntity
import com.frogtest.movieguru.data.cache.entity.search.MovieSearchEntity

@Dao
interface MovieSearchDao {

    @Upsert
    suspend fun insertAll(movies: List<MovieSearchEntity>)


    @Query("SELECT * FROM movies_search")
    fun getMovies(): PagingSource<Int, MovieSearchEntity>

    @Query("DELETE FROM movies_search")
    suspend fun clearAll()
}