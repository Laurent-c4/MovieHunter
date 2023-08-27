package com.c4entertainment.moviehunter.data.network.apiresponses

import com.c4entertainment.moviehunter.data.network.dto.movie.MovieDto
import com.squareup.moshi.Json


data class MoviesResponse(

    @field:Json(name = "page") var page: Int? = null,
    @field:Json(name = "results") var results: List<MovieDto>? = null,
    @field:Json(name = "total_pages") var totalPages: Int? = null,
    @field:Json(name = "total_results") var totalResults: Int? = null

)