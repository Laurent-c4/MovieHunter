package com.frogtest.movieguru.data.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.frogtest.movieguru.data.cache.entity.MovieRemoteKeyEntity

@Dao
interface MovieRemoteKeyDao {

    @Query("SELECT * FROM MovieRemoteKeyEntity WHERE id = :id")
    suspend fun getRemoteKey(id: String): MovieRemoteKeyEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(remoteKeys: List<MovieRemoteKeyEntity>)

    @Query("DELETE FROM MovieRemoteKeyEntity")
    suspend fun deleteAllRemoteKeys()
}