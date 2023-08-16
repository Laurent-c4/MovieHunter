package com.frogtest.movieguru.data.network

import com.squareup.moshi.Json

data class MovieDto(

    @field:Json(name = "imdbID")
    val imdbID: String,
    @field:Json(name = "Title")
    val title: String,
    @field:Json(name = "Year")
    val year: String,
    @field:Json(name = "Poster")
    val poster: String,
    @field:Json(name = "Type")
    val type: String
)