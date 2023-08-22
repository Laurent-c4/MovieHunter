package com.frogtest.movieguru.data.network.dto.movie_details

import com.frogtest.movieguru.domain.model.movie_details.Cast
import com.frogtest.movieguru.domain.model.movie_details.Crew
import com.squareup.moshi.Json


data class CreditsDto (

    @field:Json(name ="cast" ) var cast : List<CastDto>? = listOf(),
    @field:Json(name ="crew" ) var crew : List<CrewDto>? = listOf()

)