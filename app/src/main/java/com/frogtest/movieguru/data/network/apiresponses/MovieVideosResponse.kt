package com.frogtest.movieguru.data.network.apiresponses

import com.frogtest.movieguru.data.network.dto.MovieVideoDto
import com.squareup.moshi.Json


data class MovieVideosResponse (

  @field:Json(name ="id")
  var tmdbID : Int? = null,
  @field:Json(name ="results" )
  var results : List<MovieVideoDto> = arrayListOf()

)