package com.frogtest.movieguru.data.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.frogtest.movieguru.data.cache.entity.movie_details.MovieDetailsEntity

@Dao
interface MovieDetailsDao {

    @Upsert
    suspend fun insertAll(movies: List<MovieDetailsEntity>)

    @Insert
    suspend fun insertMovieDetails(movieDetailsEntity: MovieDetailsEntity): Long

    @Query("SELECT * FROM movie_details WHERE id = :id")
    suspend fun getMovieDetails(id: Int): MovieDetailsEntity?

    @Query("DELETE FROM movie_details WHERE id = :id")
    suspend fun deleteMovieDetails(id: Int): Int

    @Query("DELETE FROM movie_details")
    suspend fun clearAll()
}