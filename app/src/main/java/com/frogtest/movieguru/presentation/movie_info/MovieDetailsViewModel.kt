package com.frogtest.movieguru.presentation.movie_info

import android.util.Log
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frogtest.movieguru.domain.repository.AuthRepository
import com.frogtest.movieguru.domain.repository.MovieRepository
import com.frogtest.movieguru.domain.repository.UserSettingsRepository
import com.frogtest.movieguru.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: MovieRepository,
    private val authRepository: AuthRepository,
    private val userDataRepository: UserSettingsRepository,
) : ViewModel() {

    private val TAG = "MovieDetailsViewModel"

    var state by mutableStateOf(MovieDetailsState())

    val settingsUiState: StateFlow<SettingsUiState> =
        userDataRepository.userSettings
            .map { userData ->
                SettingsUiState.Success(
                    settings = UserEditableSettings(
                        showVideos = userData.showVideos,
                    ),
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = SettingsUiState.Loading,
            )

    private val _tabIndex =  mutableStateOf(0)
    val tabIndex = _tabIndex
    val tabs = listOf("About", "Videos")

    var isSwipeToTheLeft: Boolean = false
    private val draggableState = DraggableState { delta ->
        isSwipeToTheLeft = delta > 0
    }

    private val _dragState = mutableStateOf(draggableState)
    val dragState = _dragState

    fun updateTabIndexBasedOnSwipe() {
        _tabIndex.value = when (isSwipeToTheLeft) {
            true -> Math.floorMod(_tabIndex.value!!.plus(1), tabs.size)
            false -> Math.floorMod(_tabIndex.value!!.minus(1), tabs.size)
        }
    }

    fun updateTabIndex(i: Int) {
        _tabIndex.value = i
    }

    fun toggleShowVideos(show: Boolean) {
        viewModelScope.launch {
            userDataRepository.toggleShowVideos(show)
        }
    }

    fun toggleView(isGrid: Boolean) {
        viewModelScope.launch {
            userDataRepository.useGrid(isGrid)
        }
    }

    init {
        val id = savedStateHandle.get<String>("id") ?: "0"
        val type = savedStateHandle.get<String>("type") ?: "movie"

        getMovie(id = id.toInt(), type = type)
    }

    val getSignedInUser get() = authRepository.getSignedInUser()

    fun onEvent(event: MovieDetailsEvent) {
        when (event) {
            is MovieDetailsEvent.OnStart -> {}
            is MovieDetailsEvent.OnRefresh -> {}
        }
    }

    fun getMovie(
        id: Int,
        type: String
    ) {
        viewModelScope.launch {
            repository
                .getMovie(id = id, type = type)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { movie ->
                                state = state.copy(
                                    movie = movie,
                                    isLoading = false,
                                    error = null
                                )
                                Log.d(TAG, "getMovie ID: ${movie.id}")

                                getMovieDetails(
                                    id = movie.id,
                                    type = movie.mediaType,
                                    fetchFromNetwork = true)
                            }
                        }

                        is Resource.Error -> {
                            state = state.copy(
                                error = result.errorMessage ?: "An unexpected error occurred",
                                isLoading = false,
                                movie = null
                            )

                        }

                        is Resource.Loading -> {
                            state = state.copy(isLoading = result.isLoading)
                        }

                        else -> Unit
                    }

                }
        }

    }

    fun getMovieDetails(
        id: Int,
        type: String,
        fetchFromNetwork: Boolean = false
    ) {

        viewModelScope.launch {
            repository
                .getMovieDetails(
                    fetchFromNetwork = fetchFromNetwork,
                    id = id,
                    type = type
                )
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { movieDetails ->
                                state = state.copy(
                                    movieDetails = movieDetails
                                )

                                Log.d(TAG, "getMovieDetails id: ${movieDetails.id}")
                            }
                        }

                        is Resource.Error -> {
//                            state = state.copy(
//                                error = result.errorMessage ?: "An unexpected error occurred",
//                                isLoading = false,
//                                movieDetails = null
//                            )

                        }

                        is Resource.Loading -> {
//                            state = state.copy(isLoading = result.isLoading)
                        }
                    }

                }
        }

    }


}

/**
 * Represents the settings which the user can edit within the app.
 */
data class UserEditableSettings(
    val showVideos: Boolean = false,
)

sealed interface SettingsUiState {
    object Loading : SettingsUiState
    data class Success(val settings: UserEditableSettings) : SettingsUiState
}