package com.frogtest.movieguru.data.cache.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MovieRemoteKeyEntity (
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val prevPage: Int?,
    val nextPage: Int?
)