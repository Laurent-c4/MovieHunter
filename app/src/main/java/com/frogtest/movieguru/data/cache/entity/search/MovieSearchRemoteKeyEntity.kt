package com.frogtest.movieguru.data.cache.entity.search

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_search_remote_keys")
data class MovieSearchRemoteKeyEntity (
    @PrimaryKey(autoGenerate = false)
    val id: Int?,
    val prevPage: Int?,
    val nextPage: Int?
)