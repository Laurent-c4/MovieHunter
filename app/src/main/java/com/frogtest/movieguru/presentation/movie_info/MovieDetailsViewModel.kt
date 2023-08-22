package com.frogtest.movieguru.presentation.movie_info

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frogtest.movieguru.domain.repository.AuthRepository
import com.frogtest.movieguru.domain.repository.MovieRepository
import com.frogtest.movieguru.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: MovieRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val TAG = "MovieDetailsViewModel"

    var state by mutableStateOf(MovieDetailsState())

    init {
        val id = savedStateHandle.get<String>("imdbID") ?: ""

        getMovie(id = id.toInt())
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
    ) {
        viewModelScope.launch {
            repository
                .getMovie(id = id)
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