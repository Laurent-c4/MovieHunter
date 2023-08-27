package com.c4entertainment.moviehunter.presentation.movie_info

sealed class MovieDetailsEvent {
    object OnStart : MovieDetailsEvent()
    object OnRefresh : MovieDetailsEvent()
}
