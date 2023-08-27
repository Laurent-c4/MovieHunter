package com.c4entertainment.moviehunter.data.network.dto.movie_details

import com.squareup.moshi.Json
data class ProductionCompanyDto (

  @field:Json(name = "id"             ) var id            : Int?    = null,
  @field:Json(name = "logo_path"      ) var logoPath      : String? = null,
  @field:Json(name = "name"           ) var name          : String? = null,
  @field:Json(name = "origin_country" ) var originCountry : String? = null

)