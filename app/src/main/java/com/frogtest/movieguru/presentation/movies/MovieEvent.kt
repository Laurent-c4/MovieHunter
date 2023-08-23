package com.frogtest.movieguru.presentation.movies

sealed class MovieEvent {
    data class OnSearchQueryChange(val query: String): MovieEvent()

    data class OnMovieTVToggled(val movieTV: String): MovieEvent()

    data class OnMovieTVBackupStored(val movieTV: String): MovieEvent()
    data class OnSortToggled(val sort: Boolean): MovieEvent()

    data class OnToggleView(val isGrid: Boolean): MovieEvent()
    object OnSearchInitiated: MovieEvent()
}
