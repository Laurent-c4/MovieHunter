package com.frogtest.movieguru.data.cache.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.frogtest.movieguru.data.cache.entity.MovieEntity

@Dao
interface MovieDao {

    @Upsert
    suspend fun insertAll(movies: List<MovieEntity>)

    // Select movies with release date greater that 2000
    @Query("SELECT * FROM MovieEntity WHERE year > 2000 ORDER BY year ASC")
    fun pagingSource(): PagingSource<Int, MovieEntity>

   //Get item count
    @Query("SELECT COUNT(*) FROM MovieEntity")
    suspend fun getCount(): Int

    @Query("DELETE FROM MovieEntity")
    suspend fun clearAll()
}