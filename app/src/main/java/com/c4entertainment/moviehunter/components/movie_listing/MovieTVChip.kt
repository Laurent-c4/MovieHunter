package com.c4entertainment.moviehunter.components.movie_listing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.c4entertainment.moviehunter.components.Chip
import com.c4entertainment.moviehunter.util.MovieTVFilterConfig

@Composable
fun MovieTVChip(
    movieTV: String,
    toggleMovie: () -> Unit,
    toggleTV: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Chip(
            selected = movieTV == MovieTVFilterConfig.MOVIE,
            onSelectedChange = toggleMovie,
        ) {
            Text(text = "Movies")
        }

        Chip(
            selected = movieTV == MovieTVFilterConfig.TV,
            onSelectedChange = toggleTV,
        ) {
            Text(text = "TV Shows")
        }
    }
}