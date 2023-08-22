package com.frogtest.movieguru.presentation.movie_info

import com.frogtest.movieguru.domain.model.movie_details.MovieDetails
import com.frogtest.movieguru.domain.model.movie.Movie

data class MovieDetailsState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val movie: Movie? = null,
    val error: String? = null,

    val movieDetails: MovieDetails? = null,


)
