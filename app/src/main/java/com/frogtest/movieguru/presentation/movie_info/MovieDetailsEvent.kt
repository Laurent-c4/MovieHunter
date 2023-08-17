package com.frogtest.movieguru.presentation.movie_info

sealed class MovieDetailsEvent {
    object OnStart : MovieDetailsEvent()
    object OnRefresh : MovieDetailsEvent()
}
