package com.frogtest.movieguru.domain.model.movie_details


data class MovieDetails(

    val adult: Boolean? = null,
    val backdropPath: String? = null,
    val belongsToCollection: BelongsToCollection? = null,
    val budget: Int? = null,
    val genres: List<Genre> = arrayListOf(),
    val homepage: String? = null,
    val id: Int,
    val imdbId: String? = null,
    val originalLanguage: String? = null,
    val originalTitle: String? = null,
    val overview: String? = null,
    val popularity: Double? = null,
    val posterPath: String? = null,
    val productionCompanies: List<ProductionCompany> = listOf(),
    val productionCountries: List<ProductionCountry> = listOf(),
    val releaseDate: String? = null,
    val revenue: Long? = null,
    val runtime: Int? = null,
    val spokenLanguages: List<SpokenLanguages> = arrayListOf(),
    val status: String? = null,
    val tagline: String? = null,
    val title: String? = null,
    val video: Boolean? = null,
    val voteAverage: Double? = null,
    val voteCount: Int? = null,
    val videos: Videos? = null,
    val credits: Credits? = null

)