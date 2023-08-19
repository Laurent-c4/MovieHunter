package com.frogtest.movieguru.presentation.movie_info

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MovieDetailsScreen(
    imdbID: String,
    viewModel: MovieDetailsViewModel
) {

    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val state = viewModel.state
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = state.isLoading)


    if (state.error == null) {
        Column(Modifier.fillMaxSize()) {

            SwipeRefresh(
                modifier = Modifier.weight(1f),
                state = swipeRefreshState,
                onRefresh = { viewModel.getMovieDetails(imdbID) },
                indicator = { refreshState, refreshTrigger ->
                    SwipeRefreshIndicator(
                        state = refreshState,
                        refreshTriggerDistance = refreshTrigger,
                        backgroundColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                },
            ) {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    state.movieDetails?.let { movieDetails ->
                        item {
                            movieDetails.poster?.let {
                                AsyncImage(
                                    model = it,
                                    contentDescription = "Poster",
                                    modifier = Modifier
                                        .height(250.dp)
                                )
                            }
                        }

                        item {
                            Divider(Modifier.padding(top = 8.dp, bottom = 16.dp))
                        }

                        item {
                            movieDetails.title?.let {
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.titleLarge,
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.padding(8.dp))
                        }

                        item {
                            movieDetails.plot?.let {
                                Text(
                                    text = it,
                                    Modifier.padding(start = 8.dp, end = 8.dp)
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.padding(8.dp))
                        }

                        item {
                            movieDetails.imdbRating?.let {
                                Text(
                                    text = "IMDB Rating: $it",
                                    Modifier.padding(start = 8.dp, end = 8.dp)
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.padding(8.dp))
                        }

                        item {
                            movieDetails.released?.let {
                                Text(
                                    text = "Released: $it",
                                    Modifier.padding(start = 8.dp, end = 8.dp)
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.padding(8.dp))
                        }

                        item {
                            movieDetails.actors?.let {
                                Text(
                                    text = "Cast: $it",
                                    Modifier.padding(start = 8.dp, end = 8.dp)
                                )
                            }
                        }

                        item {
                            Divider(Modifier.padding(top = 16.dp, bottom = 8.dp))
                        }
                    }

                }
            }

            val ytVideos = state.movieVideos.filter { it.site == "YouTube" }

            AnimatedVisibility(
                visible = ytVideos.isNotEmpty(),

                ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {

                    Box {

                        HorizontalPager(
                            pageCount = ytVideos.size,
                            state = pagerState,
                            key = { ytVideos[it].key!! }
                        ) { index ->
                            YoutubePlayer(youtubeVideoID = ytVideos[index].key!!)
                        }

                    }

                    if (ytVideos.size > 1) {
                        Box(
                            modifier = Modifier
                                .offset(y = -(16).dp)
                                .fillMaxWidth(0.5f)
                                .clip(RoundedCornerShape(100))
                                .background(MaterialTheme.colorScheme.background)
//                    .padding(8.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                        {
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                    }
                                },
                                modifier = Modifier.align(Alignment.CenterStart)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowLeft,
                                    contentDescription = "Previous"
                                )
                            }

                            IconButton(
                                onClick = {
                                    scope.launch {
                                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                    }
                                },
                                modifier = Modifier.align(Alignment.CenterEnd)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowRight,
                                    contentDescription = "Next"
                                )
                            }
                        }
                    }

                }
            }

        }

    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (state.isLoading) {
            CircularProgressIndicator()
        } else if (state.error != null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error
                )
                // TODO: Add retry button
//                Button(onClick = { // TODO:
//                     }) {
//                    Text(text = "Retry")
//                }
            }
        }
    }


}