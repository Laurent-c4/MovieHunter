package com.frogtest.movieguru.components.movie_listing.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.frogtest.movieguru.components.movie_listing.item.MovieListItem
import com.frogtest.movieguru.domain.model.movie.Movie

@Composable
fun ListContent(
    movies: LazyPagingItems<Movie>,
    onNavigateToMovieDetail: (Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(
            count = movies.itemCount,
            key = movies.itemKey { it.id }
        ) {
            val item = movies[it]

            item?.let { movie ->
                MovieListItem(
                    movie = movie,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            onNavigateToMovieDetail(movie.id)
                        }
                )
            }

        }
        item {
            if (movies.loadState.append is LoadState.Loading)
                Box(Modifier.fillMaxSize()) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
        }
    }
}