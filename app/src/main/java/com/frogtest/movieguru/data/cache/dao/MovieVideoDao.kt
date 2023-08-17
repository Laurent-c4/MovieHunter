package com.frogtest.movieguru.data.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.frogtest.movieguru.data.cache.entity.MovieVideoEntity

@Dao
interface MovieVideoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movieVideos: List<MovieVideoEntity>)

    @Query("SELECT * FROM MovieVideoEntity WHERE tmdbID = :tmdbID")
    suspend fun getMovieVideos(tmdbID: Int): List<MovieVideoEntity>

    @Query("DELETE FROM MovieVideoEntity WHERE tmdbID = :tmdbID")
    suspend fun deleteMovieVideos(tmdbID: Int)
    
    @Query("DELETE FROM MovieVideoEntity")
    suspend fun clearAll()
}