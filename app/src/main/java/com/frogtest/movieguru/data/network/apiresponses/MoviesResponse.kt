package com.frogtest.movieguru.data.network.apiresponses

import com.frogtest.movieguru.data.network.dto.MovieDto
import com.squareup.moshi.Json

data class MoviesResponse(
    @field:Json(name = "Search")
    val data: List<MovieDto>,
    @field:Json(name = "totalResults")
    val totalResults: String,
    @field:Json(name = "Response")
    val response: String
)
