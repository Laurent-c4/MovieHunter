package com.frogtest.movieguru.data.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.frogtest.movieguru.data.cache.entity.MovieDetailsEntity
import com.frogtest.movieguru.data.cache.entity.MovieEntity

@Dao
interface MovieDetailsDao {

    @Upsert
    suspend fun insertAll(movies: List<MovieEntity>)

    @Insert
    suspend fun insertMovieDetails(movieDetailsEntity: MovieDetailsEntity): Long

    // Select single movie by imdbid
    @Query("SELECT * FROM MovieDetailsEntity WHERE imdbID = :imdbID")
    suspend fun getMovieDetails(imdbID: String): MovieDetailsEntity?

    // Delete single movie by imdbid
    @Query("DELETE FROM MovieDetailsEntity WHERE imdbID = :imdbID")
    suspend fun deleteMovieDetails(imdbID: String): Int

    //Update tmdbID for a movie by imdbID
    @Query("UPDATE MovieDetailsEntity SET tmdbID = :tmdbID WHERE imdbID = :imdbID")
    suspend fun setTMDBID(imdbID: String, tmdbID: Int): Int

    @Query("DELETE FROM MovieDetailsEntity")
    suspend fun clearAll()
}