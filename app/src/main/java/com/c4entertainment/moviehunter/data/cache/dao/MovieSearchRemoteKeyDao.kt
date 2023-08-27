package com.c4entertainment.moviehunter.data.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.c4entertainment.moviehunter.data.cache.entity.movie.MovieRemoteKeyEntity
import com.c4entertainment.moviehunter.data.cache.entity.search.MovieSearchRemoteKeyEntity

@Dao
interface MovieSearchRemoteKeyDao {

    @Query("SELECT * FROM movie_search_remote_keys WHERE id = :id")
    suspend fun getRemoteKey(id: Int): MovieSearchRemoteKeyEntity

    @Query("SELECT * FROM movie_search_remote_keys")
    suspend fun getAllRemoteKeys(): List<MovieRemoteKeyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(remoteKeys: List<MovieSearchRemoteKeyEntity>)

    @Query("DELETE FROM movie_search_remote_keys")
    suspend fun deleteAllRemoteKeys()
}