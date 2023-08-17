package com.frogtest.movieguru.data.network.dto

import com.squareup.moshi.Json

data class MovieRatingsDto (

  @field:Json(name ="Source" ) var source : String? = null,
  @field:Json(name ="Value"  ) var value  : String? = null

)