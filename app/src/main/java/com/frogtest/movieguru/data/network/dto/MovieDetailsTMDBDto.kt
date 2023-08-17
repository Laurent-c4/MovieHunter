package com.frogtest.movieguru.data.network.dto

import com.squareup.moshi.Json

data class MovieDetailsTMDBDto (

    @field:Json(name ="movie_results")
    var movieResults : List<MovieResultsTMDBDto>,

    )