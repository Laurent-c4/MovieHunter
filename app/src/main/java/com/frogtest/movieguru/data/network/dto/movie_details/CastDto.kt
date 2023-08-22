package com.frogtest.movieguru.data.network.dto.movie_details

import com.squareup.moshi.Json


data class CastDto (

  @field:Json( name = "adult"                ) var adult              : Boolean? = null,
  @field:Json( name = "gender"               ) var gender             : Int?     = null,
  @field:Json( name = "id"                   ) var id                 : Int?     = null,
  @field:Json( name = "known_for_department" ) var knownForDepartment : String?  = null,
  @field:Json( name = "name"                 ) var name               : String?  = null,
  @field:Json( name = "original_name"        ) var originalName       : String?  = null,
  @field:Json( name = "popularity"           ) var popularity         : Double?  = null,
  @field:Json( name = "profile_path"         ) var profilePath        : String?  = null,
  @field:Json( name = "cast_id"              ) var castId             : Int?     = null,
  @field:Json( name = "character"            ) var character          : String?  = null,
  @field:Json( name = "credit_id"            ) var creditId           : String?  = null,
  @field:Json( name = "order"                ) var order              : Int?     = null

)