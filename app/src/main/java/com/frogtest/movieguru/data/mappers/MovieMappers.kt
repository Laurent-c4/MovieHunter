package com.frogtest.movieguru.data.mappers

import com.frogtest.movieguru.data.cache.MovieEntity
import com.frogtest.movieguru.data.domain.Movie
import com.frogtest.movieguru.data.network.MovieDto

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