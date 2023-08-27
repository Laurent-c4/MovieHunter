package com.c4entertainment.moviehunter.data.cache.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.c4entertainment.moviehunter.data.cache.entity.movie.MovieEntity

@Dao
interface MovieDao {

    @Upsert
    suspend fun insertAll(movies: List<MovieEntity>)


    @Query("SELECT * FROM movies")
    fun getMovies(): PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM movies WHERE id LIKE :id")
    suspend fun getMovie(id: Int): MovieEntity?

   //Get item count
    @Query("SELECT COUNT(*) FROM movies")
    suspend fun getCount(): Int

    @Query("DELETE FROM movies")
    suspend fun clearAll()
}