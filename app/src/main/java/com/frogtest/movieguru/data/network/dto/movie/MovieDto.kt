package com.frogtest.movieguru.data.network.dto.movie

import com.squareup.moshi.Json

data class MovieDto(
    @field:Json(name = "adult") var adult: Boolean? = null,
    @field:Json(name = "backdrop_path") var backdropPath: String? = null,
    @field:Json(name = "id") var id: Int,
    @field:Json(name = "title") var title: String? = null,
    @field:Json(name = "name") var name: String? = null,
    @field:Json(name = "original_language") var originalLanguage: String? = null,
    @field:Json(name = "original_title") var originalTitle: String? = null,
    @field:Json(name = "original_name") var originalName: String? = null,
    @field:Json(name = "overview") var overview: String? = null,
    @field:Json(name = "poster_path") var posterPath: String? = null,
    @field:Json(name = "media_type") var mediaType: String? = null,
    @field:Json(name = "genre_ids") var genreIds: List<Int> = listOf(),
    @field:Json(name = "popularity") var popularity: Double? = null,
    @field:Json(name = "release_date") var releaseDate: String? = null,
    @field:Json(name = "first_air_date") var firstAirDate: String? = null,
    @field:Json(name = "video") var video: Boolean? = null,
    @field:Json(name = "vote_average") var voteAverage: Double? = null,
    @field:Json(name = "vote_count") var voteCount: Int? = null

)