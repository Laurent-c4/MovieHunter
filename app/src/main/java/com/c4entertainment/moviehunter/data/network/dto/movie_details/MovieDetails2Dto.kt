package com.c4entertainment.moviehunter.data.network.dto.movie_details

import com.squareup.moshi.Json

data class MovieDetails2Dto (

    @field:Json(name ="movie_results")
    var movieResults : List<MovieDetailsDto>,

    )