package com.frogtest.movieguru.data.network

import com.squareup.moshi.Json

data class MovieResponse(
    @field:Json(name = "Search")
    val data: List<MovieDto>,
    @field:Json(name = "totalResults")
    val totalResults: String,
    @field:Json(name = "Response")
    val response: String
)
