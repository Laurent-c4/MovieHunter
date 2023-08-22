package com.frogtest.movieguru.data.cache.entity.movie

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_remote_keys")
data class MovieRemoteKeyEntity (
    @PrimaryKey(autoGenerate = false)
    val id: Int?,
    val prevPage: Int?,
    val nextPage: Int?
)