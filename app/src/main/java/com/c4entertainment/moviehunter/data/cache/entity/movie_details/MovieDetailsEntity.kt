package com.c4entertainment.moviehunter.data.cache.entity.movie_details

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.c4entertainment.moviehunter.domain.model.movie_details.BelongsToCollection
import com.c4entertainment.moviehunter.domain.model.movie_details.Credits
import com.c4entertainment.moviehunter.domain.model.movie_details.Genre
import com.c4entertainment.moviehunter.domain.model.movie_details.ProductionCompany
import com.c4entertainment.moviehunter.domain.model.movie_details.ProductionCountry
import com.c4entertainment.moviehunter.domain.model.movie_details.SpokenLanguages
import com.c4entertainment.moviehunter.domain.model.movie_details.Videos


@Entity(tableName = "movie_details")
data class MovieDetailsEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val adult: Boolean? = null,
    val backdropPath: String? = null,
    val belongsToCollection: BelongsToCollection? = null,
    val budget: Int? = null,
    val genres: List<Genre> = arrayListOf(),
    val homepage: String? = null,
    val imdbId: String? = null,
    val originalLanguage: String? = null,
    val originalTitle: String? = null,
    val originalName: String? = null,
    val overview: String? = null,
    val popularity: Double? = null,
    val posterPath: String? = null,
    val productionCompanies: List<ProductionCompany> = arrayListOf(),
    val productionCountries: List<ProductionCountry> = arrayListOf(),
    val releaseDate: String? = null,
    val firstAirDate: String? = null,
    val revenue: Long? = null,
    val runtime: Int? = null,
    val spokenLanguages: List<SpokenLanguages> = arrayListOf(),
    val status: String? = null,
    val tagline: String? = null,
    val title: String? = null,
    val name: String? = null,
    val video: Boolean? = null,
    val voteAverage: Double? = null,
    val voteCount: Int? = null,
    val videos: Videos? = null,
    val credits: Credits? = null

)