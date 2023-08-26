package com.frogtest.movieguru.data.mappers

import com.frogtest.movieguru.data.cache.entity.movie_details.MovieDetailsEntity
import com.frogtest.movieguru.data.cache.entity.movie.MovieEntity
import com.frogtest.movieguru.data.cache.entity.search.MovieSearchEntity
import com.frogtest.movieguru.domain.model.movie.Movie
import com.frogtest.movieguru.data.network.dto.movie.MovieDto
import com.frogtest.movieguru.data.network.dto.movie_details.MovieDetailsDto
import com.frogtest.movieguru.domain.model.movie_details.BelongsToCollection
import com.frogtest.movieguru.domain.model.movie_details.Cast
import com.frogtest.movieguru.domain.model.movie_details.Credits
import com.frogtest.movieguru.domain.model.movie_details.Crew
import com.frogtest.movieguru.domain.model.movie_details.Genre
import com.frogtest.movieguru.domain.model.movie_details.MovieDetails
import com.frogtest.movieguru.domain.model.movie_details.ProductionCompany
import com.frogtest.movieguru.domain.model.movie_details.ProductionCountry

//import com.frogtest.movieguru.domain.model.MovieVideo

fun MovieDto.toMovieEntity(): MovieEntity {
    return MovieEntity(
        pk = id.toString(),
        id = id,
        adult = adult,
        backdropPath = backdropPath,
        title = title,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        posterPath = posterPath,
        mediaType = mediaType,
        genreIds = genreIds,
        popularity = popularity,
        releaseDate = releaseDate,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount,
        name = name,
        originalName = originalName,
        firstAirDate = firstAirDate
    )
}

fun MovieDto.toMovieSearchEntity(): MovieSearchEntity {
    return MovieSearchEntity(
        pk = id.toString(),
        id = id,
        adult = adult,
        backdropPath = backdropPath,
        title = title,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        posterPath = posterPath,
        mediaType = mediaType,
        genreIds = genreIds,
        popularity = popularity,
        releaseDate = releaseDate,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount,
        name = name,
        originalName = originalName,
        firstAirDate = firstAirDate
    )
}

fun MovieDto.toMovie(): Movie{
    return  Movie(
        id = id,
        adult = adult?:false,
        backdropPath = backdropPath?:"",
        title = title?:name?:"",
        originalLanguage = originalLanguage?:"",
        originalTitle = originalTitle?:originalName?:"",
        overview = overview?:"",
        posterPath = posterPath?:"",
        mediaType = mediaType?:"",
        genreIds = genreIds,
        popularity = popularity?:0.0,
        releaseDate = releaseDate?:firstAirDate?:"",
        video = video?:false,
        voteAverage = voteAverage?:0.0,
        voteCount = voteCount?:0,
    )
}

fun MovieEntity.toMovie(): Movie {
    return Movie(
        id = id,
        adult = adult ?: false,
        backdropPath = backdropPath ?: "",
        title = title ?: name ?: "",
        originalLanguage = originalLanguage ?: "",
        originalTitle = originalTitle ?: originalName ?: "",
        overview = overview ?: "",
        posterPath = posterPath ?: "",
        mediaType = mediaType ?: "",
        genreIds = genreIds,
        popularity = popularity ?: 0.0,
        releaseDate = releaseDate ?: firstAirDate ?: "",
        video = video ?: false,
        voteAverage = voteAverage ?: 0.0,
        voteCount = voteCount ?: 0,
    )
}

fun MovieSearchEntity.toMovie(): Movie {
    return Movie(
        id = id,
        adult = adult ?: false,
        backdropPath = backdropPath ?: "",
        title = title ?: name ?: "",
        originalLanguage = originalLanguage ?: "",
        originalTitle = originalTitle ?: originalName ?: "",
        overview = overview ?: "",
        posterPath = posterPath ?: "",
        mediaType = mediaType ?: "",
        genreIds = genreIds,
        popularity = popularity ?: 0.0,
        releaseDate = releaseDate ?: firstAirDate ?: "",
        video = video ?: false,
        voteAverage = voteAverage ?: 0.0,
        voteCount = voteCount ?: 0,
    )
}

fun MovieDetailsDto.toMovieDetailsEntity(): MovieDetailsEntity {
    return MovieDetailsEntity(
        id = id,
        adult = adult,
        backdropPath = backdropPath,
        belongsToCollection = belongsToCollection.let {
            BelongsToCollection(
                it?.id,
                it?.name,
                it?.posterPath,
                it?.backdropPath
            )
        },
        budget = budget,
        genres = genres.map { Genre(it.id, it.name) },
        homepage = homepage,
        imdbId = imdbId,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        productionCompanies = productionCompanies.map {
            ProductionCompany(
                it.id,
                it.logoPath,
                it.name,
                it.originCountry
            )
        },
        productionCountries = productionCountries.map { ProductionCountry(it.iso31661, it.name) },
        releaseDate = releaseDate,
        firstAirDate = firstAirDate,
        revenue = revenue,
        runtime = runtime,
        spokenLanguages = spokenLanguages,
        status = status,
        tagline = tagline,
        title = title,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount,
        videos = videos,
        credits = Credits(
            credits?.cast?.map {
                Cast(
                    id = it.id,
                    name = it.name,
                    profilePath = it.profilePath,
                    )
            },
            credits?.crew?.map {
                Crew(
                    id = it.id,
                    name = it.name,
                    profilePath = it.profilePath,
                )
            }
        ),
        name = name,
        originalName = originalName
    )
}

fun MovieDetailsEntity.toMovieDetails(): MovieDetails {
    return MovieDetails(
        id = id,
        adult = adult,
        backdropPath = backdropPath,
        belongsToCollection = belongsToCollection,
        budget = budget,
        genres = genres.map { Genre(it.id, it.name) },
        homepage = homepage,
        imdbId = imdbId,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle ?: originalName ?: "",
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        productionCompanies = productionCompanies,
        productionCountries = productionCountries,
        releaseDate = releaseDate ?: firstAirDate ?: "",
        revenue = revenue,
        runtime = runtime,
        spokenLanguages = spokenLanguages,
        status = status,
        tagline = tagline,
        title = title ?: name ?: "",
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount,
        videos = videos,
        credits = credits,
    )
}