package com.frogtest.movieguru.data.cache.entity.search

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies_search")
data class MovieSearchEntity(
    @PrimaryKey(autoGenerate = false)
    val pk: String,
    val id: Int,
    val adult: Boolean? = null,
    val backdropPath: String? = null,
    val title: String? = null,
    val name: String? = null,
    val originalLanguage: String? = null,
    val originalTitle: String? = null,
    val originalName: String? = null,
    val overview: String? = null,
    val posterPath: String? = null,
    val mediaType: String? = null,
    val genreIds: List<Int> = listOf(),
    val popularity: Double? = null,
    val releaseDate: String? = null,
    val firstAirDate: String? = null,
    val video: Boolean? = null,
    val voteAverage: Double? = null,
    val voteCount: Int? = null

)