package com.frogtest.movieguru.data.cache.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class MovieVideoEntity (

  @PrimaryKey(autoGenerate = true)
  val id: Int = 0,
  val iso6391     : String?  = null,
  val iso31661    : String?  = null,
  val name        : String?  = null,
  val key         : String?  = null,
  val site        : String?  = null,
  val size        : Int?     = null,
  val type        : String?  = null,
  val official    : Boolean? = null,
  val publishedAt : String?  = null,
  val movId          : String?  = null,
  val tmdbID          : Int?  = null

)