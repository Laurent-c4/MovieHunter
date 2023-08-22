package com.frogtest.movieguru.data.network.dto.movie_details

import com.squareup.moshi.Json


data class SpokenLanguagesDto (

  @field:Json(name = "english_name" ) var englishName : String? = null,
  @field:Json(name = "iso_639_1"    ) var iso6391     : String? = null,
  @field:Json(name = "name"         ) var name        : String? = null

)