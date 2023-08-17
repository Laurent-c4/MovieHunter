package com.frogtest.movieguru.data.cache.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity
data class MovieDetailsEntity (
    @PrimaryKey(autoGenerate = true)
     val id: Int = 0,
     val title      : String?            = null,
     val year       : String?            = null,
     val rated      : String?            = null,
     val released   : String?            = null,
     val runtime    : String?            = null,
     val genre      : String?            = null,
     val director   : String?            = null,
     val writer     : String?            = null,
     val actors     : String?            = null,
     val plot       : String?            = null,
     val language   : String?            = null,
     val country    : String?            = null,
     val awards     : String?            = null,
     val poster     : String?            = null,
//    val ratings    : ArrayList<MovieRatingsDto> = arrayListOf(),
     val metascore  : String?            = null,
     val imdbRating : String?            = null,
     val imdbVotes  : String?            = null,
     val imdbID     : String?            = null,
     val tmdbID: Int? = null,
     val type       : String?            = null,
     val dvd        : String?            = null,
     val boxOffice  : String?            = null,
     val production : String?            = null,
     val website    : String?            = null,
     val response   : String?            = null

)