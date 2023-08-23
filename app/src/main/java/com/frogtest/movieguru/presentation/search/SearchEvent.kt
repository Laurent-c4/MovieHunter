package com.frogtest.movieguru.presentation.search

sealed class SearchEvent {
    data class OnSearchQueryChange(val query: String): SearchEvent()
    data class OnSortToggled(val sort: Boolean): SearchEvent()
    data class OnToggleView(val isGrid: Boolean): SearchEvent()
    object OnSearchInitiated: SearchEvent()
    object ClearSearch: SearchEvent()
}
