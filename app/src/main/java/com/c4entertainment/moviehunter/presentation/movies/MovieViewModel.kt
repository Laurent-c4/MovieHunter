package com.c4entertainment.moviehunter.presentation.movies

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.c4entertainment.moviehunter.data.mappers.toMovie
import com.c4entertainment.moviehunter.domain.model.movie.Movie
import com.c4entertainment.moviehunter.domain.repository.AuthRepository
import com.c4entertainment.moviehunter.domain.repository.MovieRepository
import com.c4entertainment.moviehunter.domain.repository.UserSettingsRepository
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
class MovieViewModel @Inject constructor(
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

    fun onMovieEvent(event: MovieEvent) {
        when (event) {
            is MovieEvent.OnSearchQueryChange -> {
                if (searchQuery.value != event.query) {
                    updateSearchQuery(event.query)
                    searchJob?.cancel()
                    searchJob = viewModelScope.launch {
//                        getMovies(userSettingsRepository.userSettings.first().sort, event.query)
                    }
                }
            }

            is MovieEvent.OnMovieTVToggled -> {
                viewModelScope.launch {
                    userSettingsRepository.setMovieTV(event.movieTV)
                    getMovies(event.movieTV)
                }
            }

            is MovieEvent.OnMovieTVBackupStored -> {
            viewModelScope.launch {
                userSettingsRepository.setMovieTVBackup(event.movieTV)
            }
        }

            is MovieEvent.OnSortToggled -> {
                viewModelScope.launch {
                    userSettingsRepository.toggleSort(event.sort)
//                    getMovies(event.sort, searchQuery.value)
                }
            }

            is MovieEvent.OnSearchInitiated -> {
                viewModelScope.launch {
//                    getMovies(userSettingsRepository.userSettings.first().sort, searchQuery.value)
                }
            }

            is MovieEvent.OnToggleView -> {
                viewModelScope.launch {
                    toggleView(event.isGrid)
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            getMovies(userSettingsRepository.userSettings.first().movieTV)
        }
    }


    private fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }


    private fun getMovies(type: String) {
        viewModelScope.launch {
            repository.getMovies(
                type = type
            ).map { pagingData ->
                pagingData.map { movieEntity ->
                    movieEntity.toMovie()
                }
            }.cachedIn(viewModelScope).collect {
                _searchedMovies.value = it
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
)

sealed interface SettingsUiState {
    object Loading : SettingsUiState
    data class Success(val settings: UserEditableSettings) : SettingsUiState
}