package com.frogtest.movieguru.components.movie_listing.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.frogtest.movieguru.components.movie_listing.item.MovieGridItem
import com.frogtest.movieguru.domain.model.movie.Movie

@Composable
fun GridContent(
    movies: LazyPagingItems<Movie>,
    onNavigateToMovieDetail: (Int) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp),
    ) {
        items(
            count = movies.itemCount,
            key = movies.itemKey { it.id },
            contentType = movies.itemContentType { it }
        ) { index ->

            val item = movies[index]

            item?.let { movie ->
                MovieGridItem(
                    movie = movie,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 1.dp, end = 1.dp, bottom = 5.dp)
                        .clickable {
                            onNavigateToMovieDetail(movie.id)
                        }
                )
            }
        }
        item {
            if (movies.loadState.append is LoadState.Loading)
                Box(Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(36.dp)
                    )
                }

        }
    }
}