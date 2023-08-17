package com.frogtest.movieguru.presentation.movie_info

import com.frogtest.movieguru.domain.model.MovieDetails
import com.frogtest.movieguru.domain.model.MovieVideo

data class MovieDetailsState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val movieDetails: MovieDetails? = null,
    val error: String? = null,

    val isLoadingVideos: Boolean = false,
    val movieVideos: List<MovieVideo> = emptyList(),
    val errorVideos: String? = null

)
