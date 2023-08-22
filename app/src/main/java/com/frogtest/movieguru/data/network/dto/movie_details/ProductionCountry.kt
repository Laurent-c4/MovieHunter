package com.frogtest.movieguru.data.network.dto.movie_details

import com.squareup.moshi.Json


data class ProductionCountry (

  @field:Json(name = "iso_3166_1" ) var iso31661 : String? = null,
  @field:Json(name = "name"       ) var name     : String? = null

)