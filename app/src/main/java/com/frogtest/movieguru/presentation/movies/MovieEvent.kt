package com.frogtest.movieguru.presentation.movies

sealed class MovieEvent {
    data class OnSearchQueryChange(val query: String): MovieEvent()
    data class OnSortToggled(val sort: Boolean): MovieEvent()
    object OnSearchInitiated: MovieEvent()
}
