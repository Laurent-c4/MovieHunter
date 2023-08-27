package com.c4entertainment.moviehunter.data.network.dto.movie_details

import com.c4entertainment.moviehunter.domain.model.movie_details.MovieVideo
import com.squareup.moshi.Json


data class VideosDto (

  @field:Json(name = "results" ) var results : ArrayList<MovieVideo> = arrayListOf()

)