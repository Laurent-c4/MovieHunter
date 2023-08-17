package com.frogtest.movieguru.data.network.dto

import com.squareup.moshi.Json

data class MovieDetailsDto (

    @field:Json(name ="Title"      ) var title      : String?            = null,
    @field:Json(name ="Year"       ) var year       : String?            = null,
    @field:Json(name ="Rated"      ) var rated      : String?            = null,
    @field:Json(name ="Released"   ) var released   : String?            = null,
    @field:Json(name ="Runtime"    ) var runtime    : String?            = null,
    @field:Json(name ="Genre"      ) var genre      : String?            = null,
    @field:Json(name ="Director"   ) var director   : String?            = null,
    @field:Json(name ="Writer"     ) var writer     : String?            = null,
    @field:Json(name ="Actors"     ) var actors     : String?            = null,
    @field:Json(name ="Plot"       ) var plot       : String?            = null,
    @field:Json(name ="Language"   ) var language   : String?            = null,
    @field:Json(name ="Country"    ) var country    : String?            = null,
    @field:Json(name ="Awards"     ) var awards     : String?            = null,
    @field:Json(name ="Poster"     ) var poster     : String?            = null,
//    @field:Json(name ="Ratings"    ) var ratings    : ArrayList<MovieRatingsDto> = arrayListOf(),
    @field:Json(name ="Metascore"  ) var metascore  : String?            = null,
    @field:Json(name ="imdbRating" ) var imdbRating : String?            = null,
    @field:Json(name ="imdbVotes"  ) var imdbVotes  : String?            = null,
    @field:Json(name ="imdbID"     ) var imdbID     : String?            = null,
    var tmdbID: Int? = null,
    @field:Json(name ="Type"       ) var type       : String?            = null,
    @field:Json(name ="DVD"        ) var dvd        : String?            = null,
    @field:Json(name ="BoxOffice"  ) var boxOffice  : String?            = null,
    @field:Json(name ="Production" ) var production : String?            = null,
    @field:Json(name ="Website"    ) var website    : String?            = null,
    @field:Json(name ="Response"   ) var response   : String?            = null

)