package com.frogtest.movieguru.data.cache

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val imdbID: String,
    val title: String,
    val year: String,
    val poster: String,
    val type: String
)