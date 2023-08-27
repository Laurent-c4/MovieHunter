package com.c4entertainment.moviehunter.data.network.dto.movie_details

import com.c4entertainment.moviehunter.domain.model.movie_details.SpokenLanguages
import com.c4entertainment.moviehunter.domain.model.movie_details.Videos
import com.squareup.moshi.Json


data class MovieDetailsDto(

    @field:Json(name = "adult") var adult: Boolean? = null,
    @field:Json(name = "backdrop_path") var backdropPath: String? = null,
    @field:Json(name = "belongs_to_collection") var belongsToCollection: BelongsToCollectionDto? = null,
    @field:Json(name = "budget") var budget: Int? = null,
    @field:Json(name = "genres") var genres: List<GenresDto> = listOf(),
    @field:Json(name = "homepage") var homepage: String? = null,
    @field:Json(name = "id") var id: Int,
    @field:Json(name = "imdb_id") var imdbId: String? = null,
    @field:Json(name = "original_language") var originalLanguage: String? = null,
    @field:Json(name = "original_title") var originalTitle: String? = null,
    @field:Json(name = "original_name") var originalName: String? = null,
    @field:Json(name = "overview") var overview: String? = null,
    @field:Json(name = "popularity") var popularity: Double? = null,
    @field:Json(name = "poster_path") var posterPath: String? = null,
    @field:Json(name = "production_companies") var productionCompanies: List<ProductionCompanyDto> = listOf(),
    @field:Json(name = "production_countries") var productionCountries: List<ProductionCountry> = listOf(),
    @field:Json(name = "release_date") var releaseDate: String? = null,
    @field:Json(name = "first_air_date") var firstAirDate: String? = null,
    @field:Json(name = "revenue") var revenue: Long? = null,
    @field:Json(name = "runtime") var runtime: Int? = null,
    @field:Json(name = "spoken_languages") var spokenLanguages: List<SpokenLanguages> = listOf(),
    @field:Json(name = "status") var status: String? = null,
    @field:Json(name = "tagline") var tagline: String? = null,
    @field:Json(name = "title") var title: String? = null,
    @field:Json(name = "name") var name: String? = null,
    @field:Json(name = "video") var video: Boolean? = null,
    @field:Json(name = "vote_average") var voteAverage: Double? = null,
    @field:Json(name = "vote_count") var voteCount: Int? = null,
    @field:Json(name = "videos") var videos: Videos? = null,
    @field:Json(name = "credits") var credits: CreditsDto? = null

)