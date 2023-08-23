package com.frogtest.movieguru.presentation.search

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.frogtest.movieguru.data.mappers.toMovie
import com.frogtest.movieguru.domain.model.movie.Movie
import com.frogtest.movieguru.domain.repository.AuthRepository
import com.frogtest.movieguru.domain.repository.MovieRepository
import com.frogtest.movieguru.domain.repository.UserSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val userSettingsRepository: UserSettingsRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    val TAG = "MovieViewModel"

    val settingsUiState: StateFlow<SettingsUiState> =
        userSettingsRepository.userSettings
            .map { userData ->
                SettingsUiState.Success(
                    settings = UserEditableSettings(
                        sort = userData.sort,
                        movieTV = userData.movieTV,
                        useGrid = userData.useGrid,
                    ),
                )
            }
            .stateIn(
                scope = viewModelScope,
                // Starting eagerly means the user data is ready when the SettingsDialog is laid out
                // for the first time. Without this, due to b/221643630 the layout is done using the
                // "Loading" text, then replaced with the user editable fields once loaded, however,
                // the layout height doesn't change meaning all the fields are squashed into a small
                // scrollable column.
                // TODO: Change to SharingStarted.WhileSubscribed(5_000) when b/221643630 is fixed
                started = SharingStarted.Eagerly,
                initialValue = SettingsUiState.Loading,
            )

    private var searchJob: Job? = null

    private val _searchQuery = mutableStateOf("")
    val searchQuery = _searchQuery

    private val _searchedMovies = MutableStateFlow<PagingData<Movie>>(PagingData.empty())

    val searchedMovies = _searchedMovies

    val getSignedInUser get() = authRepository.getSignedInUser()

    fun onSearchEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnSearchQueryChange -> {
                if (searchQuery.value != event.query) {
                    updateSearchQuery(event.query)
                    searchJob?.cancel()
                    searchJob = viewModelScope.launch {
                        searchMovies(event.query, userSettingsRepository.userSettings.first().movieTV)
                    }
                }
            }

            is SearchEvent.OnSortToggled -> {
                viewModelScope.launch {
                    userSettingsRepository.toggleSort(event.sort)
                    searchMovies(searchQuery.value, userSettingsRepository.userSettings.first().movieTV)
                }
            }

            is SearchEvent.OnMovieTVToggled -> {
                viewModelScope.launch {
                    userSettingsRepository.setMovieTV(event.movieTV)
                    searchMovies(searchQuery.value, event.movieTV)
                }
            }

            is SearchEvent.OnSearchInitiated -> {
                viewModelScope.launch {
                    searchMovies(searchQuery.value, userSettingsRepository.userSettings.first().movieTV)
                }
            }

            is SearchEvent.OnToggleView -> {
                viewModelScope.launch {
                    toggleView(event.isGrid)
                }
            }

            is SearchEvent.ClearSearch -> {
                viewModelScope.launch {
                    clearSearch()
                }
            }
        }
    }

    private fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }


    private fun searchMovies(query: String, type: String) {
        if(query.isBlank()) return
        viewModelScope.launch {
            repository.searchMovies(
                query = query,
                type = type,
            )
                .cachedIn(viewModelScope).map {
                    it.map { movieSearchEntity ->
                        movieSearchEntity.toMovie()
                    }
                }
                .collect {
                _searchedMovies.value = it
            }
        }
    }

    private fun clearSearch() {
        viewModelScope.launch {
            repository.clearSearch().collect{
                _searchedMovies.value = PagingData.empty()
            }
        }
    }

    private fun toggleView(isGrid: Boolean) {
        viewModelScope.launch {
            userSettingsRepository.useGrid(isGrid)
        }
    }

}

data class UserEditableSettings(
    val sort: Boolean,
    val movieTV: String,
    val useGrid: Boolean,
)

sealed interface SettingsUiState {
    object Loading : SettingsUiState
    data class Success(val settings: UserEditableSettings) : SettingsUiState
}