package com.frogtest.movieguru.presentation.movie_info

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frogtest.movieguru.domain.repository.MovieRepository
import com.frogtest.movieguru.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: MovieRepository
): ViewModel() {

    private val TAG = "MovieDetailsViewModel"

    var state by mutableStateOf(MovieDetailsState())

    init {
        val imdbID = savedStateHandle.get<String>("imdbID") ?: ""

        getMovieDetails(imdbID = imdbID)
    }

    fun onEvent(event: MovieDetailsEvent) {
        when(event) {
            is MovieDetailsEvent.OnStart -> {}
            is MovieDetailsEvent.OnRefresh -> {}
        }
    }

    private fun getMovieDetails(
        imdbID: String,
        fetchFromNetwork: Boolean = false
        ) {

        viewModelScope.launch {
            repository
                .getMovieDetails(
                fetchFromNetwork = fetchFromNetwork,
                imdbID = imdbID)
                .collect { result ->
                    when(result) {
                        is Resource.Success -> {
                            result.data?.let { movieDetails ->
                                state = state.copy(
                                    movieDetails = movieDetails
                                )

                                Log.d(TAG, "getMovieDetails tmdbID: ${movieDetails.tmdbID}")
                                movieDetails.tmdbID?.let { tmdbID ->
                                    getMovieVideos(tmdbID = tmdbID)
                                }
                            }
                        }
                        is Resource.Error -> {
                            state = state.copy(
                                error = result.errorMessage ?: "An unexpected error occurred",
                                isLoading = false,
                                movieDetails = null
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

    private fun getMovieVideos(
        tmdbID: Int,
        fetchFromNetwork: Boolean = false
    ) {

        viewModelScope.launch {
            repository
                .getMovieVideos(
                    fetchFromNetwork = fetchFromNetwork,
                    tmdbID = tmdbID)
                .collect { result ->
                    when(result) {
                        is Resource.Success -> {
                            result.data?.let { movieVideos ->
                                state = state.copy(
                                    movieVideos = movieVideos
                                )
                            }
                        }
                        is Resource.Error -> {
                            state = state.copy(
                                errorVideos = result.errorMessage ?: "An unexpected error occurred",
                                isLoadingVideos = false,
                                movieDetails = null
                            )

                        }
                        is Resource.Loading -> {
                            state = state.copy(isLoadingVideos = result.isLoading)
                        }
                        else -> Unit
                    }

                }
        }

    }
}