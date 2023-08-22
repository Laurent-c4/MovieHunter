package com.frogtest.movieguru.data.network.dto.movie_details

import com.frogtest.movieguru.domain.model.movie_details.MovieVideo
import com.squareup.moshi.Json


data class VideosDto (

  @field:Json(name = "results" ) var results : ArrayList<MovieVideo> = arrayListOf()

)