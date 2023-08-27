package com.c4entertainment.moviehunter.presentation.movie_info

import com.c4entertainment.moviehunter.domain.model.movie_details.MovieDetails
import com.c4entertainment.moviehunter.domain.model.movie.Movie

data class MovieDetailsState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val movie: Movie? = null,
    val error: String? = null,

    val movieDetails: MovieDetails? = null,


)
