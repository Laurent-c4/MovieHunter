package com.c4entertainment.moviehunter.data.network.dto.movie_details
import com.squareup.moshi.Json


data class MovieVideoDto (

  @field:Json(name = "iso_639_1"    ) var iso6391     : String?  = null,
  @field:Json(name = "iso_3166_1"   ) var iso31661    : String?  = null,
  @field:Json(name = "name"         ) var name        : String?  = null,
  @field:Json(name = "key"          ) var key         : String?  = null,
  @field:Json(name = "site"         ) var site        : String?  = null,
  @field:Json(name = "size"         ) var size        : Int?     = null,
  @field:Json(name = "type"         ) var type        : String?  = null,
  @field:Json(name = "official"     ) var official    : Boolean? = null,
  @field:Json(name = "published_at" ) var publishedAt : String?  = null,
  @field:Json(name = "id"           ) var id          : String?  = null

)