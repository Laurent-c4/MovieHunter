package com.frogtest.movieguru.presentation.movie_info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun MovieDetailsScreen(
    imdbID: String,
    viewModel: MovieDetailsViewModel
) {

    val state = viewModel.state
    if (state.error == null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            state.movieDetails?.let { movieDetails ->
                movieDetails.title?.let { Text(text = it) }
                movieDetails.plot?.let { Text(text = it) }

            }

            state.movieVideos.forEach { movieVideo ->
                if (movieVideo.site == "YouTube") {
                    movieVideo.key?.let { YoutubePlayer(youtubeVideoID = it) }
                }
            }

        }
    } else {

    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (state.isLoading) {
            CircularProgressIndicator()
        } else if(state.error != null) {
            Text(
                text = state.error,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}