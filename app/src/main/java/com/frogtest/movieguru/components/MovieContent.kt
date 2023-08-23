package com.frogtest.movieguru.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.frogtest.movieguru.components.movie_listing.MovieTVChip
import com.frogtest.movieguru.components.movie_listing.PagingDataErrorWidget
import com.frogtest.movieguru.components.movie_listing.PagingDataNoResultsWidget
import com.frogtest.movieguru.components.movie_listing.view.GridContent
import com.frogtest.movieguru.components.movie_listing.view.ListContent
import com.frogtest.movieguru.domain.model.movie.Movie
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun MovieContent(
    modifier: Modifier,
    moviesOrTV: String,
    content: LazyPagingItems<Movie>,
    toggleMovie: () -> Unit,
    toggleTV: () -> Unit,
    useGrid: Boolean,
    onClickMovie: (Int) -> Unit,
) {
    val swipeRefreshState =
        rememberSwipeRefreshState(isRefreshing = content.loadState.refresh is LoadState.Loading)

    Column(
        modifier = modifier
    ) {
        MovieTVChip(
            movieTV = moviesOrTV,
            toggleMovie = toggleMovie,
            toggleTV = toggleTV,
        )

        Box(
            modifier = Modifier
                .fillMaxSize()

        ) {

            if (content.loadState.refresh is LoadState.Loading) {
//                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (content.itemCount == 0) {
                if (content.loadState.refresh is LoadState.Error) {
                    PagingDataErrorWidget(
                        retry = { content.refresh() },
                        errorMessage = (content.loadState.refresh as LoadState.Error).error.message
                    )
                } else {
                    PagingDataNoResultsWidget()
                }
            } else {

                SwipeRefresh(
                    state = swipeRefreshState,
                    onRefresh = { content.refresh() },
                    indicator = { state, refreshTrigger ->
                        SwipeRefreshIndicator(
                            state = state,
                            refreshTriggerDistance = refreshTrigger,
                            backgroundColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                ) {

                    if (useGrid)
                        GridContent(
                            movies = content,
                            onNavigateToMovieDetail = { id ->
                                onClickMovie(id)
                            }
                        )
                    else
                        ListContent(
                            movies = content,
                            onNavigateToMovieDetail = { id ->
                                onClickMovie(id)
                            }
                        )
                }
            }
        }
    }
}