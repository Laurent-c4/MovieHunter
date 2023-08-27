package com.c4entertainment.moviehunter.domain.model.movie

data class Movie(
    val adult: Boolean,
    val backdropPath: String,
    val id: Int,
    val title: String,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val posterPath: String,
    val mediaType: String,
    val genreIds: List<Int> = listOf(),
    val popularity: Double,
    val releaseDate: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int

)