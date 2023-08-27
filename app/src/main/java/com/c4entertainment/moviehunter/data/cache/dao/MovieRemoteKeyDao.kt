package com.c4entertainment.moviehunter.data.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.c4entertainment.moviehunter.data.cache.entity.movie.MovieRemoteKeyEntity

@Dao
interface MovieRemoteKeyDao {

    @Query("SELECT * FROM movie_remote_keys WHERE id = :id")
    suspend fun getRemoteKey(id: Int): MovieRemoteKeyEntity

    @Query("SELECT * FROM movie_remote_keys")
    suspend fun getAllRemoteKeys(): List<MovieRemoteKeyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(remoteKeys: List<MovieRemoteKeyEntity>)

    @Query("DELETE FROM movie_remote_keys")
    suspend fun deleteAllRemoteKeys()
}