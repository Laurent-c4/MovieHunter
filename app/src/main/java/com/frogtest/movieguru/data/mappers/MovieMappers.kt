package com.frogtest.movieguru.data.mappers

import com.frogtest.movieguru.data.cache.entity.MovieDetailsEntity
import com.frogtest.movieguru.data.cache.entity.MovieEntity
import com.frogtest.movieguru.data.cache.entity.MovieVideoEntity
import com.frogtest.movieguru.data.network.dto.MovieDetailsDto
import com.frogtest.movieguru.domain.model.Movie
import com.frogtest.movieguru.data.network.dto.MovieDto
import com.frogtest.movieguru.data.network.dto.MovieVideoDto
import com.frogtest.movieguru.domain.model.MovieDetails
import com.frogtest.movieguru.domain.model.MovieVideo

fun MovieDto.toMovieEntity(): MovieEntity {
    return MovieEntity(
        imdbID = imdbID,
        title = title,
        year = year,
        poster = poster,
        type = type
    )
}

fun MovieEntity.toMovie(): Movie {
    return Movie(
        imdbID = imdbID,
        title = title,
        year = year,
        poster = poster,
        type = type
    )
}

fun MovieDetailsDto.toMovieDetailsEntity(): MovieDetailsEntity {
    return MovieDetailsEntity(
        title = title,
        year = year,
        rated = rated,
        released = released,
        runtime = runtime,
        genre = genre,
        director = director,
        writer = writer,
        actors = actors,
        plot = plot,
        language = language,
        country = country,
        awards = awards,
        poster = poster,
//        ratings = ratings,
        metascore = metascore,
        imdbRating = imdbRating,
        imdbVotes = imdbVotes,
        imdbID = imdbID,
        tmdbID = tmdbID,
        type = type,
        dvd = dvd,
        boxOffice = boxOffice,
        production = production,
        website = website,
        response = response
    )
}

fun MovieDetailsEntity.toMovieDetails(): MovieDetails {
    return MovieDetails(
        title = title,
        year = year,
        rated = rated,
        released = released,
        runtime = runtime,
        genre = genre,
        director = director,
        writer = writer,
        actors = actors,
        plot = plot,
        language = language,
        country = country,
        awards = awards,
        poster = poster,
//        ratings = ratings,
        metascore = metascore,
        imdbRating = imdbRating,
        imdbVotes = imdbVotes,
        imdbID = imdbID,
        tmdbID = tmdbID,
        type = type,
        dvd = dvd,
        boxOffice = boxOffice,
        production = production,
        website = website,
        response = response
    )
}

fun MovieVideoDto.toMovieVideoEntity(): MovieVideoEntity {
    return MovieVideoEntity(
        key = key,
        name = name,
        site = site,
        size = size,
        type = type,
        tmdbID = tmdbID
    )
}

fun MovieVideoEntity.toMovieVideo(): MovieVideo {
    return MovieVideo(
        key = key,
        name = name,
        site = site,
        size = size,
        type = type,
        tmdbID = tmdbID
    )
}