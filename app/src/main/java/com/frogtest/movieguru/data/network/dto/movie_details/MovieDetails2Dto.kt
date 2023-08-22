package com.frogtest.movieguru.data.network.dto.movie_details

import com.frogtest.movieguru.data.network.dto.movie_details.MovieDetailsDto
import com.squareup.moshi.Json

data class MovieDetails2Dto (

    @field:Json(name ="movie_results")
    var movieResults : List<MovieDetailsDto>,

    )