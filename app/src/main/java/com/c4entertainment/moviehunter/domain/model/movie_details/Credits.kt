package com.c4entertainment.moviehunter.domain.model.movie_details


data class Credits(

    val cast: List<Cast>? = listOf(),
    val crew: List<Crew>? = listOf()

)